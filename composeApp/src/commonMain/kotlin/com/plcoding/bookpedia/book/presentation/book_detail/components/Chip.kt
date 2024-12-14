package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

private val ChipHorizontalPadding = 16.dp
private val ChipVerticalPadding = 8.dp

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    containerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentPadding: PaddingValues = PaddingValues(horizontal = ChipHorizontalPadding, vertical = ChipVerticalPadding),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        ProvideContentColorTextStyle(
            contentColor = contentColor,
            textStyle = MaterialTheme.typography.labelLarge
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

/**
 * ProvideContentColorTextStyle
 *
 * A convenience method to provide values to both LocalContentColor and LocalTextStyle in one call.
 * This is less expensive than nesting calls to CompositionLocalProvider.
 *
 * Text styles will be merged with the current value of LocalTextStyle.
 */
@Composable
internal fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}