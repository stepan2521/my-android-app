package myfirst.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import myfirst.app.ui.theme.Color
import kotlin.math.floor

@Composable
fun ClickButton(modifier: Modifier = Modifier) {
    var money by remember { mutableFloatStateOf(0f) }
    var addPerClick by remember { mutableFloatStateOf(1f) }

    val text = if (money - floor(money) == 0f) {
        "Money: ${money.toInt()}"
    } else {
        "Money: $money"
    }

    Button(
        onClick = {
            money += addPerClick
        },
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.5.dp, Color.Black),
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