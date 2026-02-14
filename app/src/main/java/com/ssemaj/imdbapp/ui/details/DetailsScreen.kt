package com.ssemaj.imdbapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssemaj.imdbapp.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ssemaj.imdbapp.data.model.MovieDetails
import com.ssemaj.imdbapp.data.model.Genre
import com.ssemaj.imdbapp.data.model.ProductionCompany
import com.ssemaj.imdbapp.data.model.SpokenLanguage
import com.ssemaj.imdbapp.ui.components.ErrorMessage
import com.ssemaj.imdbapp.ui.components.LoadingIndicator
import com.ssemaj.imdbapp.ui.theme.ImdbAppTheme
import com.ssemaj.imdbapp.ui.util.isLandscape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    movieId: Int,
    viewModel: DetailsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is DetailsUiState.Success) {
                        Text(
                            text = (uiState as DetailsUiState.Success).movie.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DetailsUiState.Loading -> {
                    LoadingIndicator()
                }
                is DetailsUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.retry(movieId) }
                    )
                }
                is DetailsUiState.Success -> {
                    MovieDetailsContent(uiState = state)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MovieDetailsContent(
    uiState: DetailsUiState.Success,
    modifier: Modifier = Modifier
) {
    val isLandscape = isLandscape()
    
    if (isLandscape) {
        LandscapeDetailsContent(uiState = uiState, modifier = modifier)
    } else {
        PortraitDetailsContent(uiState = uiState, modifier = modifier)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LandscapeDetailsContent(
    uiState: DetailsUiState.Success,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        // Left side - Image
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = uiState.posterUrl.ifEmpty { uiState.backdropUrl },
                contentDescription = uiState.movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.7f to Color.Transparent,
                                1.0f to MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }
        
        // Right side - Details
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DetailsTextContent(uiState = uiState)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PortraitDetailsContent(
    uiState: DetailsUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Backdrop image with gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        ) {
            AsyncImage(
                model = uiState.backdropUrl.ifEmpty { uiState.posterUrl },
                contentDescription = uiState.movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                0.75f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                1.0f to MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            DetailsTextContent(uiState = uiState)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsTextContent(uiState: DetailsUiState.Success) {
    Text(
        text = uiState.movie.title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )

    uiState.movie.tagline?.takeIf { it.isNotBlank() }?.let { tagline ->
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "\"$tagline\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Light
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoChipSurface(
            icon = Icons.Default.Star,
            text = stringResource(R.string.rating_format, uiState.formattedRating),
            iconTint = MaterialTheme.colorScheme.primary,
            highlighted = true
        )
        InfoChipSurface(
            icon = Icons.Default.CalendarToday,
            text = uiState.formattedYear
        )
        if (uiState.formattedRuntime.isNotEmpty()) {
            InfoChipSurface(
                icon = Icons.Default.AccessTime,
                text = uiState.formattedRuntime
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    if (uiState.movie.genres.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.movie.genres.forEach { genre ->
                SuggestionChip(
                    onClick = { },
                    label = { Text(genre.name, style = MaterialTheme.typography.labelMedium) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    border = null
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    SectionHeader(title = stringResource(R.string.overview))
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = uiState.movie.overview.ifBlank { stringResource(R.string.no_overview_available) },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    if (uiState.movie.budget > 0 || uiState.movie.revenue > 0) {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(title = stringResource(R.string.box_office))
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (uiState.movie.budget > 0) {
                    InfoRow(label = stringResource(R.string.budget), value = uiState.formattedBudget)
                }
                if (uiState.movie.budget > 0 && uiState.movie.revenue > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (uiState.movie.revenue > 0) {
                    InfoRow(label = stringResource(R.string.revenue), value = uiState.formattedRevenue)
                }
            }
        }
    }

    if (uiState.movie.productionCompanies.isNotEmpty()) {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(title = stringResource(R.string.production))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = uiState.formattedProductionCompanies,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@Composable
private fun InfoChipSurface(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    highlighted: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (highlighted) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        tonalElevation = 1.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = iconTint
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = if (highlighted) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private val previewMovieDetails = MovieDetails(
    id = 1,
    title = "The Dark Knight",
    overview = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
    posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
    backdropPath = "/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg",
    voteAverage = 8.5,
    voteCount = 25000,
    releaseDate = "2008-07-18",
    runtime = 152,
    status = "Released",
    tagline = "Why So Serious?",
    budget = 185_000_000L,
    revenue = 1_004_558_444L,
    genres = listOf(
        Genre(id = 28, name = "Action"),
        Genre(id = 80, name = "Crime"),
        Genre(id = 18, name = "Drama")
    ),
    productionCompanies = listOf(
        ProductionCompany(id = 1, name = "Warner Bros.", logoPath = null, originCountry = "US"),
        ProductionCompany(id = 2, name = "Legendary Pictures", logoPath = null, originCountry = "US")
    ),
    spokenLanguages = listOf(
        SpokenLanguage(englishName = "English", iso6391 = "en", name = "English")
    )
)

@Preview(showBackground = true)
@Composable
private fun PortraitDetailsContentPreview() {
    ImdbAppTheme {
        val formatter = com.ssemaj.imdbapp.domain.MovieFormatter()
        val imageConfig = com.ssemaj.imdbapp.domain.ImageConfig()
        val uiState = DetailsUiState.Success(
            movie = previewMovieDetails,
            formattedRating = formatter.formatRating(previewMovieDetails.voteAverage),
            formattedYear = formatter.extractYear(previewMovieDetails.releaseDate),
            formattedRuntime = formatter.formatRuntime(previewMovieDetails.runtime),
            formattedBudget = formatter.formatCurrency(previewMovieDetails.budget),
            formattedRevenue = formatter.formatCurrency(previewMovieDetails.revenue),
            formattedProductionCompanies = formatter.formatProductionCompanies(previewMovieDetails.productionCompanies),
            posterUrl = imageConfig.posterUrl(previewMovieDetails.posterPath),
            backdropUrl = imageConfig.backdropUrl(previewMovieDetails.backdropPath)
        )
        PortraitDetailsContent(uiState = uiState)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LandscapeDetailsContentPreview() {
    ImdbAppTheme {
        val formatter = com.ssemaj.imdbapp.domain.MovieFormatter()
        val imageConfig = com.ssemaj.imdbapp.domain.ImageConfig()
        val uiState = DetailsUiState.Success(
            movie = previewMovieDetails,
            formattedRating = formatter.formatRating(previewMovieDetails.voteAverage),
            formattedYear = formatter.extractYear(previewMovieDetails.releaseDate),
            formattedRuntime = formatter.formatRuntime(previewMovieDetails.runtime),
            formattedBudget = formatter.formatCurrency(previewMovieDetails.budget),
            formattedRevenue = formatter.formatCurrency(previewMovieDetails.revenue),
            formattedProductionCompanies = formatter.formatProductionCompanies(previewMovieDetails.productionCompanies),
            posterUrl = imageConfig.posterUrl(previewMovieDetails.posterPath),
            backdropUrl = imageConfig.backdropUrl(previewMovieDetails.backdropPath)
        )
        LandscapeDetailsContent(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoChipSurfacePreview() {
    ImdbAppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoChipSurface(
                icon = Icons.Default.Star,
                text = stringResource(R.string.rating_format, "8.5"),
                highlighted = true
            )
            InfoChipSurface(
                icon = Icons.Default.CalendarToday,
                text = "2008"
            )
            InfoChipSurface(
                icon = Icons.Default.AccessTime,
                text = "2h 32m"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsTextContentPreview() {
    ImdbAppTheme {
        val formatter = com.ssemaj.imdbapp.domain.MovieFormatter()
        val imageConfig = com.ssemaj.imdbapp.domain.ImageConfig()
        val uiState = DetailsUiState.Success(
            movie = previewMovieDetails,
            formattedRating = formatter.formatRating(previewMovieDetails.voteAverage),
            formattedYear = formatter.extractYear(previewMovieDetails.releaseDate),
            formattedRuntime = formatter.formatRuntime(previewMovieDetails.runtime),
            formattedBudget = formatter.formatCurrency(previewMovieDetails.budget),
            formattedRevenue = formatter.formatCurrency(previewMovieDetails.revenue),
            formattedProductionCompanies = formatter.formatProductionCompanies(previewMovieDetails.productionCompanies),
            posterUrl = imageConfig.posterUrl(previewMovieDetails.posterPath),
            backdropUrl = imageConfig.backdropUrl(previewMovieDetails.backdropPath)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            DetailsTextContent(uiState = uiState)
        }
    }
}
