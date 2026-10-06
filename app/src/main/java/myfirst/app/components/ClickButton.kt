package myfirst.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import myfirst.app.ui.theme.Color as AppColor

/**
 * @param onHit вызывается при клике/зажатии.
 *              isLongPress = true → долгое зажатие
 */
@Composable
fun ClickButton(
    moneyPerClick: Double,
    cooldownMs: Long,
    onHit: (isLongPress: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    var isOnCooldown by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(1f) }
    var flashText by remember { mutableStateOf<String?>(null) }

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

    LaunchedEffect(flashText) {
        if (flashText != null) {
            delay(600)
            flashText = null
        }
    }

    fun tryHit(longPress: Boolean) {
        if (isOnCooldown) return
        lastClickTime = System.currentTimeMillis()
        isOnCooldown = true
        onHit(longPress)
        if (longPress) flashText = "HOLD!"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = modifier.combinedClickable(
                enabled = !isOnCooldown,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { tryHit(false) },
                onLongClick = { tryHit(true) }
            ),
            shape = RoundedCornerShape(50),
            color = if (isOnCooldown) AppColor.DarkBlue else AppColor.Blue,
            border = BorderStroke(1.5.dp, AppColor.Black),
            shadowElevation = if (isOnCooldown) 0.dp else 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val display = formatCompact(moneyPerClick)
                Text(
                    text = flashText ?: if (isOnCooldown) "$display$" else "+$display$",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
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

private fun formatCompact(value: Double): String {
    return when {
        value >= 1e36 -> String.format("%.2e", value)
        value >= 1e12 -> String.format("%.2e", value)
        value >= 1e6 -> String.format("%.2fM", value / 1e6)
        value >= 1e3 -> String.format("%.1fK", value / 1e3)
        value == value.toLong().toDouble() -> value.toLong().toString()
        else -> String.format("%.1f", value)
    }
}
