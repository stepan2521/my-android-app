package myfirst.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import myfirst.app.ui.theme.Color as AppColor

@Composable
fun ClickButton(
    moneyPerClick: Float,
    cooldownMs: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    var isOnCooldown by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(isOnCooldown, lastClickTime, cooldownMs) {
        if (isOnCooldown) {
            val start = lastClickTime
            while (true) {
                val elapsed = System.currentTimeMillis() - start
                if (elapsed >= cooldownMs) {
                    isOnCooldown = false
                    progress = 1f
                    break
                }
                progress = 1f - (elapsed.toFloat() / cooldownMs)
                delay(16.milliseconds)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                if (!isOnCooldown) {
                    lastClickTime = System.currentTimeMillis()
                    isOnCooldown = true
                    onClick()
                }
            },
            enabled = !isOnCooldown,
            modifier = modifier,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.5.dp, AppColor.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.Blue,
                contentColor = Color.White,
                disabledContainerColor = AppColor.DarkBlue,
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            )
        ) {
            val display = if (moneyPerClick == moneyPerClick.toInt().toFloat()) {
                moneyPerClick.toInt().toString()
            } else {
                String.format("%.1f", moneyPerClick)
            }
            Text(if (isOnCooldown) "$display$" else "+$display$")
        }

        if (isOnCooldown && cooldownMs > 80) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.height(6.dp)
            )
        }
    }
}
