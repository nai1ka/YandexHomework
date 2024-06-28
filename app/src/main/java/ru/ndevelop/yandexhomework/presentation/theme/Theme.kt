package ru.ndevelop.yandexhomework.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


private val LightColors = lightColorScheme(
    primary = Colors.Day.blue,
    tertiary = Colors.Day.blue,
    error = Colors.Day.red,
    outline = Colors.Day.grayLight,
    outlineVariant = Colors.Day.gray,
    surface = Colors.Day.backSecondary,
    background = Colors.Day.backPrimary,
    surfaceTint = Colors.Day.backElevated,
    onPrimary = Colors.Day.labelPrimary,
    onSecondary = Colors.Day.labelSecondary ,
    onTertiary = Colors.Day.labelTertiary,
    onSurfaceVariant = Colors.Day.labelDisable,
)

private val DarkColors = darkColorScheme(
    primary = Colors.Night.blue,
    tertiary = Colors.Night.blue,
    error = Colors.Night.red,
    outline = Colors.Night.grayLight,
    outlineVariant = Colors.Night.gray,
    surface = Colors.Night.backSecondary,
    background = Colors.Night.backPrimary,
    surfaceTint = Colors.Night.backElevated,
    onPrimary = Colors.Night.labelPrimary,
    onSecondary = Colors.Night.labelSecondary ,
    onTertiary = Colors.Night.labelTertiary,
    onSurfaceVariant = Colors.Night.labelDisable,
)


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = Typography
    )
}


@Composable
fun ThemePreviewBox(backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(240.dp, 100.dp)
            .background(backgroundColor)
    ) {
        Text(text = backgroundColor.toHexString(), color = Color.Magenta, modifier = Modifier.align(
            Alignment.BottomStart
        ))
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun Color.toHexString(): String {
    return "#${value.toHexString(HexFormat.UpperCase).take(9)}"
}
@Composable
fun ThemePreviewContent(colorsToShow: List<List<Color>>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        for (i in colorsToShow.indices) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                for (j in colorsToShow[i].indices){
                    ThemePreviewBox(colorsToShow[i][j])
                }
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 240 * 6, name = "Light Theme")
@Composable
fun AppThemeLightPreview() {
    AppTheme(useDarkTheme = false) {
        ThemePreviewContent(lightColorsToShow)
    }
}

@Preview(showBackground = true, widthDp = 240 * 6, name = "Dark Theme")
@Composable
fun AppThemeDarkPreview() {
    AppTheme(useDarkTheme = true) {
        ThemePreviewContent(darkColorsToShow)
    }
}

@Preview(showBackground = true, name = "Text styles")
@Composable
fun TextStylesPreview() {
    AppTheme(useDarkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Large title - 32/38", style = MaterialTheme.typography.titleLarge)
            Text("Title - 20/32", style = MaterialTheme.typography.titleMedium)
            Text("BUTTON - 14/42", style = MaterialTheme.typography.titleSmall)
            Text("Body - 16/20", style = MaterialTheme.typography.bodyMedium)
            Text("Subhead - 14/20", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

val lightColorsToShow = listOf(
    listOf(
        Colors.Day.supportSeparator,
        Colors.Day.supportOverlay
    ),
    listOf(
        Colors.Day.labelPrimary,
        Colors.Day.labelSecondary,
        Colors.Day.labelTertiary,
        Colors.Day.labelDisable,
    ),
    listOf(
        Colors.Day.red,
        Colors.Day.green,
        Colors.Day.blue,
        Colors.Day.gray,
        Colors.Day.grayLight,
    ),
    listOf(
        Colors.Day.backPrimary,
        Colors.Day.backSecondary,
        Colors.Day.backElevated,
    ),
)

val darkColorsToShow = listOf(
    listOf(
        Colors.Night.supportSeparator,
        Colors.Night.supportOverlay
    ),
    listOf(
        Colors.Night.labelPrimary,
        Colors.Night.labelSecondary,
        Colors.Night.labelTertiary,
        Colors.Night.labelDisable,
    ),
    listOf(
        Colors.Night.red,
        Colors.Night.green,
        Colors.Night.blue,
        Colors.Night.gray,
        Colors.Night.grayLight,
    ),
    listOf(
        Colors.Night.backPrimary,
        Colors.Night.backSecondary,
        Colors.Night.backElevated,
    ),
)