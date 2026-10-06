package myfirst.app.mechanics

import myfirst.app.UpgradeType
import kotlin.math.pow

data class Upgrade(
    val type: UpgradeType,
    val level: Int = 0
) {
    /** Стоимость следующей покупки */
    fun getCost(): Long {
        return (type.baseCost * (level + 1).toDouble().pow(1.55)).toLong()
            .coerceAtLeast(type.baseCost)
    }
}
