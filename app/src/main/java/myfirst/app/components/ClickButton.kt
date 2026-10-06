package myfirst.app.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import myfirst.app.ui.theme.Color
import kotlin.math.floor

@Composable
fun ClickButton(modifier: Modifier?) {
    var money by remember { mutableFloatStateOf(0f) }
    var text by remember { mutableStateOf("Money: $money") }
    var addPerClick by remember { mutableFloatStateOf(1f) }

    Button(
        onClick = {
            money += addPerClick
            text = if (money - floor(money) == 0f) {
                "Money: ${money.toInt()}"
            } else {
                "Money: $money"
            }
        },
        modifier = modifier ?: Modifier.size(250.dp).border(1.5.dp, Color.Black),
        colors = ButtonColors(
            Color.Blue, Color.DarkBlue,
            Color.Red, Color.DarkRed
        )
    ) {
        Text(text)
    }
}