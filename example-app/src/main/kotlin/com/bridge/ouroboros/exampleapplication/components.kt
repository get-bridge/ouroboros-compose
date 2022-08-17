package com.bridge.ouroboros.exampleapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.bridge.ouroboros.exampleapplication.theme.AppTheme

@Composable
fun DrawerContent(
    examples: List<Example> = knownExamples,
    currentHierarchy: Sequence<NavDestination>,
    onExampleClicked: (Example) -> Unit
) {
    Column {
        Text(text = stringResource(R.string.examples), modifier = Modifier.padding(16.dp))

        examples.forEach { example ->
            DrawerItem(
                icon = example.icon,
                selected = currentHierarchy.any { it.route == example.route },
                text = stringResource(id = example.title),
                onClick = { onExampleClicked(example) }
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = onClick, enabled = enabled)
                .fillMaxWidth()
                .run {
                    if (selected) {
                        background(MaterialTheme.colors.primary.copy(alpha = 0.12f))
                    } else {
                        this
                    }
                }
                .padding(8.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.primary) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
@Preview(group = "Components")
fun DrawerItemPreview() {
    AppTheme {
        DrawerItem(
            icon = Icons.Default.Favorite,
            text = "Login",
            onClick = {}
        )
    }
}

@Composable
@Preview(group = "Components")
fun DrawerItemSelectedPreview() {
    AppTheme {
        DrawerItem(
            icon = Icons.Default.Favorite,
            text = "Login",
            selected = true,
            onClick = {}
        )
    }
}
