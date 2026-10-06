package myfirst.app

enum class UpgradeType {
    ADD_PER_CLICK(0, null),
    MULTIPLY_PER_CLICK(1, 20),
    REDUCE_CLICK_COOLDOWN(2, 50);

    var code: Int
    var cost: Int

    constructor(code: Int?, cost: Int?) {
        this.code = code ?: 0
        this.cost = cost ?: 20
    }


    override fun toString(): String {
        return when (code) {
            code -> "Add Per Click"
            else -> ""
        }
    }
}