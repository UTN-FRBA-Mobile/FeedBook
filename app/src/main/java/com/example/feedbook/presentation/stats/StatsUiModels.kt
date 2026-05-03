package com.example.feedbook.presentation.stats

data class StatsUiState(
    val title: String,
    val subtitle: String,
    val metrics: List<StatsMetric>,
    val heatmapMonths: List<String>,
    val heatmapRows: List<String>,
    val heatmapValues: List<List<Float>>,
    val selectedRadarMode: String,
    val radarSections: List<RadarSection>
)

data class StatsMetric(
    val label: String,
    val value: String
)

data class RadarAxis(
    val label: String,
    val value: Float
)

data class RankingItem(
    val rank: Int,
    val label: String
)

data class RadarSection(
    val mode: String,
    val axes: List<RadarAxis>,
    val ranking: List<RankingItem>
)

fun sampleStatsUiState(): StatsUiState = StatsUiState(
    title = "Reading Ledger",
    subtitle = "A comprehensive overview of your literary engagement and year-to-date metrics.",
    metrics = listOf(
        StatsMetric("BOOKS READ", "42"),
        StatsMetric("TOTAL PAGES", "12,450"),
        StatsMetric("UNIQUE AUTHORS", "38"),
        StatsMetric("GENRES EXPLORED", "12")
    ),
    heatmapMonths = listOf("April", "May", "June"),
    heatmapRows = listOf("L", "M", "M", "J", "V", "S"),
    heatmapValues = listOf(
        listOf(0.08f, 0.12f, 0.18f, 0.15f, 0.20f, 0.28f, 0.35f, 0.55f, 0.60f, 0.72f, 0.76f, 0.68f),
        listOf(0.05f, 0.10f, 0.16f, 0.12f, 0.22f, 0.36f, 0.45f, 0.58f, 0.62f, 0.75f, 0.82f, 0.74f),
        listOf(0.06f, 0.09f, 0.15f, 0.18f, 0.26f, 0.33f, 0.50f, 0.57f, 0.64f, 0.78f, 0.86f, 0.80f),
        listOf(0.04f, 0.08f, 0.14f, 0.20f, 0.29f, 0.41f, 0.47f, 0.61f, 0.67f, 0.70f, 0.78f, 0.73f),
        listOf(0.03f, 0.10f, 0.13f, 0.22f, 0.31f, 0.38f, 0.52f, 0.56f, 0.63f, 0.71f, 0.79f, 0.76f),
        listOf(0.02f, 0.07f, 0.12f, 0.18f, 0.24f, 0.30f, 0.43f, 0.50f, 0.58f, 0.66f, 0.74f, 0.69f)
    ),
    selectedRadarMode = "Genre",
    radarSections = listOf(
        RadarSection(
            mode = "Genre",
            axes = listOf(
                RadarAxis("Adventure", 0.46f),
                RadarAxis("Fantasy", 0.68f),
                RadarAxis("Sci-Fi", 0.54f),
                RadarAxis("Suspense", 0.42f),
                RadarAxis("Horror", 0.34f),
                RadarAxis("Romance", 0.52f),
                RadarAxis("Drama", 0.74f),
                RadarAxis("Mystery", 0.63f)
            ),
            ranking = listOf(
                RankingItem(1, "Drama"),
                RankingItem(2, "Science Fiction")
            )
        ),
        RadarSection(
            mode = "Author",
            axes = listOf(
                RadarAxis("Asimov", 0.72f),
                RadarAxis("Le Guin", 0.58f),
                RadarAxis("Murakami", 0.44f),
                RadarAxis("King", 0.39f),
                RadarAxis("Austen", 0.34f),
                RadarAxis("Doyle", 0.49f),
                RadarAxis("Tolkien", 0.81f),
                RadarAxis("Atwood", 0.56f)
            ),
            ranking = listOf(
                RankingItem(1, "J.R.R. Tolkien"),
                RankingItem(2, "Isaac Asimov")
            )
        )
    )
)
