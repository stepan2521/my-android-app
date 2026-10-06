package myfirst.app

import androidx.annotation.StringRes

enum class UpgradeType(
    val baseCost: Long,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int
) {
    ADD_PER_CLICK(
        baseCost = 100,
        nameRes = R.string.upgrade_add_per_click_name,
        descriptionRes = R.string.upgrade_add_per_click_desc
    ),
    MULTIPLY_PER_CLICK(
        baseCost = 500,
        nameRes = R.string.upgrade_multiply_per_click_name,
        descriptionRes = R.string.upgrade_multiply_per_click_desc
    ),
    REDUCE_COOLDOWN(
        baseCost = 250,
        nameRes = R.string.upgrade_reduce_cooldown_name,
        descriptionRes = R.string.upgrade_reduce_cooldown_desc
    ),
    CRIT_CHANCE(
        baseCost = 200,
        nameRes = R.string.upgrade_crit_chance_name,
        descriptionRes = R.string.upgrade_crit_chance_desc
    ),
    LONG_PRESS_POWER(
        baseCost = 300,
        nameRes = R.string.upgrade_long_press_name,
        descriptionRes = R.string.upgrade_long_press_desc
    );
}
