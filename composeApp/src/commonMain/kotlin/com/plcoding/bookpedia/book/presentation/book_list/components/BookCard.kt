package com.plcoding.bookpedia.book.presentation.book_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import coil3.compose.AsyncImage
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.domain.util.formatAverageRating

@Composable
fun BookCard(
    book: Book,
    isFavorite: Boolean,
    onClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick(book) }
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.large),
            model = book.imageUrl,
            contentDescription = "Cover",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 2.dp, end = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                book.averageRating?.let { rating ->
                    ElevatedAssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        append(formatAverageRating(rating))
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        append("/5")
                                    }
                                    book.ratingCount?.let { count ->
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                                fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                                            )
                                        ) {
                                            append(" ($count)")
                                        }
                                    }
                                }
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rate"
                            )
                        },
                        shape = MaterialTheme.shapes.small,
                        elevation = AssistChipDefaults.assistChipElevation(elevation = 0.dp),
                        enabled = false,
                        colors = AssistChipDefaults.assistChipColors(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            disabledLeadingIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                IconButton(
                    onClick = { onFavoriteClick(book) }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Add Favorite"
                    )
                }
            }
            Column(
                modifier = Modifier.padding(end = 14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors.fastJoinToString(","),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}