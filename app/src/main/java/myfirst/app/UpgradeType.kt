package myfirst.app

import androidx.annotation.StringRes

enum class UpgradeType(
    val baseCost: Int,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int
) {
    ADD_PER_CLICK(
        baseCost = 10,
        nameRes = R.string.upgrade_add_per_click_name,
        descriptionRes = R.string.upgrade_add_per_click_desc
    ),
    MULTIPLY_PER_CLICK(
        baseCost = 50,
        nameRes = R.string.upgrade_multiply_per_click_name,
        descriptionRes = R.string.upgrade_multiply_per_click_desc
    ),
    REDUCE_COOLDOWN(
        baseCost = 25,
        nameRes = R.string.upgrade_reduce_cooldown_name,
        descriptionRes = R.string.upgrade_reduce_cooldown_desc
    );
}
