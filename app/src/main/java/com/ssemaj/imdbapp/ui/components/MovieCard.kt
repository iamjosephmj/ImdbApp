package com.ssemaj.imdbapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.ui.theme.ImdbAppTheme
import com.ssemaj.imdbapp.domain.ImageConfig
import com.ssemaj.imdbapp.domain.MovieFormatter

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 4.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.aspectRatio(2f / 3f)
        ) {
            // Poster image or placeholder
            val imageConfig = ImageConfig()
            val posterUrl = imageConfig.posterUrl(movie.posterPath)
            if (posterUrl.isNotEmpty() && movie.posterPath != null) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            } else {
                // Placeholder for missing poster
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }

            // Gradient overlay at bottom for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                0.75f to Color.Black.copy(alpha = 0.4f),
                                1.0f to Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Rating badge - only show if rating exists (> 0)
            if (movie.voteAverage > 0) {
                val formatter = MovieFormatter()
                RatingBadge(
                    rating = formatter.formatRating(movie.voteAverage),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            // Title and year at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                val formatter = MovieFormatter()
                val year = formatter.extractYear(movie.releaseDate)
                if (year.isNotEmpty()) {
                    Text(
                        text = year,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBadge(
    rating: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = rating,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    ImdbAppTheme {
        MovieCard(
            movie = PreviewData.movie,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardNoImagePreview() {
    ImdbAppTheme {
        MovieCard(
            movie = PreviewData.movieNoImage,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun RatingBadgePreview() {
    ImdbAppTheme {
        RatingBadge(rating = "8.5")
    }
}

object PreviewData {
    val movie = Movie(
        id = 1,
        title = "The Dark Knight",
        overview = "When the menace known as the Joker wreaks havoc on Gotham...",
        posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
        backdropPath = "/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg",
        voteAverage = 8.5,
        voteCount = 25000,
        releaseDate = "2008-07-18"
    )

    val movieNoImage = Movie(
        id = 2,
        title = "Unknown Movie",
        overview = "No description available",
        posterPath = null,
        backdropPath = null,
        voteAverage = 0.0,
        voteCount = 0,
        releaseDate = null
    )
}
