package myfirst.app.mechanics

import myfirst.app.UpgradeType

data class Upgrade(
    val type: UpgradeType,
    var level: Int = 0
) {
    val name: String
        get() = type.displayName

    val description: String
        get() = type.description

    /** Стоимость следующей покупки */
    fun getCost(): Int {
        return (type.baseCost * Math.pow((level + 1).toDouble(), 1.4)).toInt()
    }
}
