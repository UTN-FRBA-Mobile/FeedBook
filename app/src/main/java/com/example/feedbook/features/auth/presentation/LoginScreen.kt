package com.example.feedbook.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.feedbook.R
import com.example.feedbook.core.ui.theme.FeedBookTheme

private object LoginColors {
    val Background = Color(0xFFFBF9F8)
    val Title = Color(0xFF0C2439)
    val Subtitle = Color(0xFF5D6067)
    val Label = Color(0xFF4E535A)
    val Placeholder = Color(0xFFB9BDC3)
    val Divider = Color(0xFFC8CDD5)
    val PrimaryButton = Color(0xFF0C2439)
    val Error = Color(0xFFB45A52)
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showCredentialsError by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LoginColors.Background,
                        Color(0xFFF7F4F1),
                        LoginColors.Background
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 38.dp)
                .navigationBarsPadding()
        ) {
            val compactLayout = maxHeight < 760.dp

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(if (compactLayout) 0.95f else 1.4f))
                LogoHeader(compact = compactLayout)
                Spacer(modifier = Modifier.weight(if (compactLayout) 0.6f else 1f))
                Column(modifier = Modifier.fillMaxWidth()) {
                    LoginTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            showCredentialsError = false
                        },
                        label = stringResource(R.string.login_email_address),
                        placeholder = stringResource(R.string.login_email_placeholder),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        isError = showCredentialsError
                    )
                    Spacer(modifier = Modifier.height(if (compactLayout) 16.dp else 28.dp))
                    LoginTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            showCredentialsError = false
                        },
                        label = stringResource(R.string.login_password),
                        placeholder = stringResource(R.string.login_password_placeholder),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = showCredentialsError
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (compactLayout) 14.dp else 20.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.login_forgot_password),
                            color = LoginColors.Label,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = if (compactLayout) 14.sp else 16.sp),
                            modifier = Modifier.clickable(onClick = onForgotPasswordClick)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (showCredentialsError) {
                            stringResource(R.string.login_credentials_error)
                        } else {
                            " "
                        },
                        color = LoginColors.Error,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(if (compactLayout) 14.dp else 22.dp))
                    Button(
                        onClick = {
                            showCredentialsError = email.isNotBlank() && password.isNotBlank() && email != password
                            if (!showCredentialsError && email.isNotBlank() && password.isNotBlank()) {
                                onSignInClick()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (compactLayout) 60.dp else 76.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LoginColors.PrimaryButton,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.login_sign_in),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = if (compactLayout) 17.sp else 19.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.6.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Login,
                                contentDescription = null,
                                modifier = Modifier.size(if (compactLayout) 20.dp else 22.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(if (compactLayout) 0.75f else 1.1f))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE8E4E0),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(if (compactLayout) 20.dp else 40.dp))
                Text(
                    text = stringResource(R.string.login_create_prompt),
                    color = LoginColors.Label,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = if (compactLayout) 18.sp else 20.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(if (compactLayout) 8.dp else 12.dp))
                Text(
                    text = stringResource(R.string.login_create_account),
                    color = LoginColors.Title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = if (compactLayout) 16.sp else 18.sp,
                        letterSpacing = 1.4.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(onClick = onCreateAccountClick)
                )
                Spacer(modifier = Modifier.weight(if (compactLayout) 0.45f else 0.65f))
            }
        }
    }
}

@Composable
private fun LogoHeader(compact: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.feedbook_logo),
            contentDescription = null,
            modifier = Modifier.size(if (compact) 58.dp else 72.dp)
        )
        Spacer(modifier = Modifier.height(if (compact) 12.dp else 18.dp))
        Text(
            text = "FeedBook",
            color = LoginColors.Title,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = if (compact) 36.sp else 44.sp,
                lineHeight = if (compact) 36.sp else 44.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.5).sp
            )
        )
        Spacer(modifier = Modifier.height(if (compact) 6.dp else 10.dp))
        Text(
            text = stringResource(R.string.login_tagline),
            color = LoginColors.Subtitle,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (compact) 10.sp else 12.sp,
                letterSpacing = if (compact) 1.5.sp else 2.2.sp
            )
        )
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = if (isError) LoginColors.Error else LoginColors.Label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = LoginColors.Placeholder,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Light
                    )
                )
            },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = LoginColors.Title,
                fontSize = 21.sp,
                fontWeight = FontWeight.Light
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedTextColor = LoginColors.Title,
                unfocusedTextColor = LoginColors.Title,
                errorTextColor = LoginColors.Title,
                cursorColor = LoginColors.Title
            ),
            isError = isError,
            shape = RoundedCornerShape(0.dp)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = if (isError) LoginColors.Error else LoginColors.Divider,
            thickness = 1.dp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF9F8, heightDp = 909, widthDp = 394)
@Composable
private fun LoginScreenPreview() {
    FeedBookTheme {
        LoginScreen()
    }
}
