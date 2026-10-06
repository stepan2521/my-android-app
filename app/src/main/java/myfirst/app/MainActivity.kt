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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import myfirst.app.components.ClickButton
import myfirst.app.components.UpgradeMenu
import myfirst.app.data.GameDatabase
import myfirst.app.data.GameRepository
import myfirst.app.data.GameStateEntity
import myfirst.app.mechanics.Upgrade
import myfirst.app.ui.theme.MainTheme
import kotlin.math.max
import kotlin.random.Random

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
    val context = LocalContext.current

    val repository = remember {
        val db = GameDatabase.getInstance(context)
        GameRepository(db.gameDao())
    }

    var isLoaded by remember { mutableStateOf(false) }

    var money by remember { mutableDoubleStateOf(0.0) }
    var basePerClick by remember { mutableDoubleStateOf(1.0) }
    var multiplier by remember { mutableDoubleStateOf(1.0) }
    var cooldownMs by remember { mutableLongStateOf(500L) }
    var critChance by remember { mutableFloatStateOf(0.05f) }
    var longPressMultiplier by remember { mutableFloatStateOf(3f) }

    var showUpgrades by remember { mutableStateOf(false) }
    var lastHitInfo by remember { mutableStateOf<String?>(null) }

    val upgrades = remember {
        mutableStateListOf(
            Upgrade(UpgradeType.ADD_PER_CLICK),
            Upgrade(UpgradeType.MULTIPLY_PER_CLICK),
            Upgrade(UpgradeType.REDUCE_COOLDOWN),
            Upgrade(UpgradeType.CRIT_CHANCE),
            Upgrade(UpgradeType.LONG_PRESS_POWER)
        )
    }

    LaunchedEffect(Unit) {
        val state = repository.getState()
        money = state.money
        basePerClick = state.basePerClick
        multiplier = state.multiplier
        cooldownMs = state.cooldownMs
        critChance = state.critChance
        longPressMultiplier = state.longPressMultiplier

        upgrades[0] = Upgrade(UpgradeType.ADD_PER_CLICK, state.addPerClickLevel)
        upgrades[1] = Upgrade(UpgradeType.MULTIPLY_PER_CLICK, state.multiplyPerClickLevel)
        upgrades[2] = Upgrade(UpgradeType.REDUCE_COOLDOWN, state.reduceCooldownLevel)
        upgrades[3] = Upgrade(UpgradeType.CRIT_CHANCE, state.critChanceLevel)
        upgrades[4] = Upgrade(UpgradeType.LONG_PRESS_POWER, state.longPressLevel)

        isLoaded = true
    }

    LaunchedEffect(
        money, basePerClick, multiplier, cooldownMs,
        critChance, longPressMultiplier, upgrades.toList()
    ) {
        if (!isLoaded) return@LaunchedEffect
        delay(300)
        repository.save(
            GameStateEntity(
                money = money,
                basePerClick = basePerClick,
                multiplier = multiplier,
                cooldownMs = cooldownMs,
                critChance = critChance,
                longPressMultiplier = longPressMultiplier,
                addPerClickLevel = upgrades[0].level,
                multiplyPerClickLevel = upgrades[1].level,
                reduceCooldownLevel = upgrades[2].level,
                critChanceLevel = upgrades[3].level,
                longPressLevel = upgrades[4].level
            )
        )
    }

    LaunchedEffect(lastHitInfo) {
        if (lastHitInfo != null) {
            delay(800)
            lastHitInfo = null
        }
    }

    val moneyPerClick = basePerClick * multiplier

    if (!isLoaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

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

            Text(
                text = stringResource(R.string.crit_chance_label, critChance * 100f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.long_press_label, longPressMultiplier),
                style = MaterialTheme.typography.bodyMedium
            )

            if (lastHitInfo != null) {
                Text(
                    text = lastHitInfo!!,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ClickButton(
                moneyPerClick = moneyPerClick,
                cooldownMs = cooldownMs,
                onHit = { isLongPress ->
                    var gain = moneyPerClick
                    val tags = mutableListOf<String>()

                    if (isLongPress) {
                        gain *= longPressMultiplier
                        tags += "HOLD x${String.format("%.1f", longPressMultiplier)}"
                    }

                    if (Random.nextFloat() < critChance) {
                        gain *= 2.0
                        tags += "CRIT x2"
                    }

                    money += gain
                    if (tags.isNotEmpty()) {
                        lastHitInfo = tags.joinToString(" + ")
                    }
                },
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.hint_hold),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showUpgrades = true }) {
                Text(stringResource(R.string.upgrades_button))
            }
        }

        if (showUpgrades) {
            UpgradeMenu(
                money = money,
                upgrades = upgrades,
                onBuy = { upgrade ->
                    val cost = upgrade.getCost().toDouble()
                    if (money >= cost) {
                        money -= cost

                        val index = upgrades.indexOfFirst { it.type == upgrade.type }
                        if (index >= 0) {
                            upgrades[index] = upgrade.copy(level = upgrade.level + 1)
                        }

                        when (upgrade.type) {
                            UpgradeType.ADD_PER_CLICK -> basePerClick += 1.0
                            UpgradeType.MULTIPLY_PER_CLICK -> multiplier *= 1.5
                            UpgradeType.REDUCE_COOLDOWN -> {
                                cooldownMs = max(50L, cooldownMs - 50L)
                            }
                            UpgradeType.CRIT_CHANCE -> {
                                critChance = (critChance + 0.02f).coerceAtMost(0.75f)
                            }
                            UpgradeType.LONG_PRESS_POWER -> {
                                longPressMultiplier += 0.5f
                            }
                        }
                    }
                },
                onDismiss = { showUpgrades = false }
            )
        }
    }
}

private fun formatMoney(value: Double): String {
    return when {
        value >= 1e12 -> String.format("%.3e$", value)
        value >= 1e6 -> String.format("%.2fM$", value / 1e6)
        value >= 1e3 -> String.format("%.1fK$", value / 1e3)
        value == value.toLong().toDouble() -> "${value.toLong()}$"
        else -> String.format("%.1f$", value)
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    MainTheme {
        Surface {
            Text("Preview")
        }
    }
}
