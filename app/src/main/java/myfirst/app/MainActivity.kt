package myfirst.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import myfirst.app.components.ClickButton
import myfirst.app.components.UpgradeMenu
import myfirst.app.mechanics.Upgrade
import myfirst.app.ui.theme.MainTheme
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
fun GameScreen() {
    var money by remember { mutableFloatStateOf(0f) }
    var basePerClick by remember { mutableFloatStateOf(1f) }
    var multiplier by remember { mutableFloatStateOf(1f) }
    var cooldownMs by remember { mutableLongStateOf(500L) } // 0.5 сек

    var showUpgrades by remember { mutableStateOf(false) }

    val upgrades = remember {
        mutableStateListOf(
            Upgrade(UpgradeType.ADD_PER_CLICK),
            Upgrade(UpgradeType.MULTIPLY_PER_CLICK),
            Upgrade(UpgradeType.REDUCE_COOLDOWN)
        )
    }

    val moneyPerClick = basePerClick * multiplier

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = formatMoney(money),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.money_per_click, formatMoney(moneyPerClick)),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(R.string.cooldown_label, cooldownMs / 1000f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            ClickButton(
                moneyPerClick = moneyPerClick,
                cooldownMs = cooldownMs,
                onClick = { money += moneyPerClick },
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { showUpgrades = true }) {
                Text(stringResource(R.string.upgrades_button))
            }
        }

        if (showUpgrades) {
            UpgradeMenu(
                money = money,
                upgrades = upgrades,
                onBuy = { upgrade ->
                    val cost = upgrade.getCost()
                    if (money >= cost) {
                        money -= cost

                        val index = upgrades.indexOf(upgrade)
                        if (index >= 0) {
                            upgrades[index] = upgrade.copy(level = upgrade.level + 1)
                        }

                        when (upgrade.type) {
                            UpgradeType.ADD_PER_CLICK -> basePerClick += 1f
                            UpgradeType.MULTIPLY_PER_CLICK -> multiplier *= 1.5f
                            UpgradeType.REDUCE_COOLDOWN -> {
                                cooldownMs = max(50L, cooldownMs - 50L)
                            }
                        }
                    }
                },
                onDismiss = { showUpgrades = false }
            )
        }
    }
}

private fun formatMoney(value: Float): String {
    return if (value == value.toInt().toFloat()) {
        "${value.toInt()}$"
    } else {
        String.format("%.1f$", value)
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    MainTheme {
        Surface {
            GameScreen()
        }
    }
}
