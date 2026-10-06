package myfirst.app

enum class UpgradeType(
    val baseCost: Int,
    val displayName: String,
    val description: String
) {
    ADD_PER_CLICK(
        baseCost = 10,
        displayName = "+1 Money per click",
        description = "Увеличивает базовый доход с клика на 1"
    ),
    MULTIPLY_PER_CLICK(
        baseCost = 50,
        displayName = "x1.5 Money per click",
        description = "Умножает весь доход с клика на 1.5"
    ),
    REDUCE_COOLDOWN(
        baseCost = 25,
        displayName = "-0.05s Cooldown",
        description = "Уменьшает задержку между кликами на 0.05 секунды"
    );
}
