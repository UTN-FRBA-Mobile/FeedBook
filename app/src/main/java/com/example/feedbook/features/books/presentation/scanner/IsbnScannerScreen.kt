package com.example.feedbook.features.books.presentation.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.OptIn
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.feedbook.features.books.domain.usecase.GetBookByIsbnUseCase
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

private const val ScannerLogTag = "FeedBookBarcodeScanner"
private const val FallbackBookId = "1"

@Composable
fun IsbnScannerScreen(
    getBookByIsbnUseCase: GetBookByIsbnUseCase,
    onClose: () -> Unit,
    onScanComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var scannedBarcode by rememberSaveable { mutableStateOf<String?>(null) }
    var isResolvingBarcode by rememberSaveable { mutableStateOf(false) }
    var hasDispatchedResult by rememberSaveable { mutableStateOf(false) }
    var statusMessage by rememberSaveable {
        mutableStateOf("Keep the barcode inside the frame.")
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(scannedBarcode, hasDispatchedResult) {
        val barcode = scannedBarcode ?: return@LaunchedEffect
        if (hasDispatchedResult) return@LaunchedEffect

        val isbn = extractIsbnFromBarcode(barcode)
            ?: barcode.filter { it.isDigit() || it == 'X' || it == 'x' }.uppercase()
        if (isbn.isBlank()) {
            scannedBarcode = null
            statusMessage = "Could not read a complete barcode. Try again."
            return@LaunchedEffect
        }

        isResolvingBarcode = true
        statusMessage = "ISBN $isbn detected. Opening book..."
        delay(5_000)

        val bookId = runCatching {
            getBookByIsbnUseCase(isbn).id
        }.getOrElse { error ->
            Log.w(ScannerLogTag, "ISBN $isbn not found. Opening fallback book.", error)
            FallbackBookId
        }

        hasDispatchedResult = true
        onScanComplete(bookId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasCameraPermission) {
            CameraPreview(
                onBarcodeDetected = { barcode ->
                    if (scannedBarcode == null && !isResolvingBarcode && !hasDispatchedResult) {
                        scannedBarcode = barcode
                        statusMessage = "Barcode detected."
                    }
                },
                onCameraAnalyzing = {
                    if (scannedBarcode == null && !isResolvingBarcode) {
                        statusMessage = "Camera ready. Point it at the barcode."
                    }
                },
                onScanError = {
                    if (scannedBarcode == null && !isResolvingBarcode) {
                        statusMessage = "Scanner error. Move the barcode and try again."
                    }
                },
                onCameraError = {
                    if (scannedBarcode == null && !isResolvingBarcode) {
                        statusMessage = "Camera error. Close the scanner and try again."
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            ScannerOverlay(
                onClose = onClose,
                scannedBarcode = scannedBarcode,
                statusMessage = statusMessage,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PermissionRequiredContent(
                onClose = onClose,
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CameraPreview(
    onBarcodeDetected: (String) -> Unit,
    onCameraAnalyzing: () -> Unit,
    onScanError: () -> Unit,
    onCameraError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember(context) { ProcessCameraProvider.getInstance(context) }
    val currentOnBarcodeDetected by rememberUpdatedState(onBarcodeDetected)
    val currentOnCameraAnalyzing by rememberUpdatedState(onCameraAnalyzing)
    val currentOnScanError by rememberUpdatedState(onScanError)
    val currentOnCameraError by rememberUpdatedState(onCameraError)
    val previewView = remember(context) {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val scanner = remember {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        BarcodeScanning.getClient(options)
    }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    DisposableEffect(previewView, lifecycleOwner, context, scanner, cameraExecutor) {
        val executor = ContextCompat.getMainExecutor(context)
        val listener = Runnable {
            runCatching {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also { it.surfaceProvider = previewView.surfaceProvider }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            BarcodeAnalyzer(
                                scanner = scanner,
                                onBarcodeDetected = { barcode ->
                                    executor.execute { currentOnBarcodeDetected(barcode) }
                                },
                                onCameraAnalyzing = {
                                    executor.execute { currentOnCameraAnalyzing() }
                                },
                                onScanError = {
                                    executor.execute { currentOnScanError() }
                                }
                            )
                        )
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            }.onFailure { error ->
                Log.e(ScannerLogTag, "Camera binding failed", error)
                currentOnCameraError()
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            if (cameraProviderFuture.isDone) {
                cameraProviderFuture.get().unbindAll()
            }
            scanner.close()
            cameraExecutor.shutdown()
        }
    }
}

@Composable
private fun ScannerOverlay(
    onClose: () -> Unit,
    scannedBarcode: String?,
    statusMessage: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Close scanner",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Surface(
                modifier = Modifier.size(250.dp),
                color = Color.Transparent,
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(3.dp, Color.White, RoundedCornerShape(28.dp))
                )
            }

            Text(
                text = if (scannedBarcode == null) "Scanning barcode..." else "Scanned barcode",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            if (scannedBarcode != null) {
                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = scannedBarcode,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

private class BarcodeAnalyzer(
    private val scanner: BarcodeScanner,
    private val onBarcodeDetected: (String) -> Unit,
    private val onCameraAnalyzing: () -> Unit,
    private val onScanError: () -> Unit
) : ImageAnalysis.Analyzer {
    private var isProcessing = false
    private var hasReportedFrame = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true
        if (!hasReportedFrame) {
            hasReportedFrame = true
            onCameraAnalyzing()
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.asSequence()
                    .mapNotNull { barcode -> barcode.rawValue ?: barcode.displayValue }
                    .filter { value -> value.isNotBlank() }
                    .firstOrNull()
                    ?.let(onBarcodeDetected)
            }
            .addOnFailureListener { error ->
                Log.e(ScannerLogTag, "Barcode processing failed", error)
                onScanError()
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }
}

@Composable
private fun PermissionRequiredContent(
    onClose: () -> Unit,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFF0D1321))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Close scanner",
                tint = Color.White
            )
        }

        Icon(
            imageVector = Icons.Outlined.QrCodeScanner,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Camera access is required",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Allow camera permission to scan a barcode and show its number.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        Button(
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Allow camera")
        }
    }
}
