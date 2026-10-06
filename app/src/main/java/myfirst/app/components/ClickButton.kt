package myfirst.app.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import myfirst.app.ui.theme.Color
import kotlin.math.floor

@Composable
fun ClickButton(modifier: Modifier = Modifier) {
    var clicks by remember { mutableFloatStateOf(0f) }
    var addPerClick by remember { mutableFloatStateOf(1f) }

    val text = if (clicks - floor(clicks) == 0f) {
        "Клики: ${clicks.toInt()}"
    } else {
        "Клики: $clicks"
    }

    Button(
        onClick = {
            clicks += addPerClick
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = androidx.compose.ui.graphics.Color.White,
            disabledContainerColor = Color.Red,
            disabledContentColor = Color.DarkRed
        )
    ) {
        Text(text)
    }
}