package myfirst.app.mechanics

import myfirst.app.UpgradeType
import kotlin.math.pow

data class Upgrade(
    val type: UpgradeType,
    var level: Int = 0
) {
    val name: String
        get() = type.displayName

    val description: String
        get() = type.description

    fun getCost(): Int {
        return (type.baseCost * (level + 1).toDouble().pow(1.4)).toInt()
    }
}
