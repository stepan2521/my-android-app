package myfirst.app.mechanics

import myfirst.app.UpgradeType

class Upgrade {
    var type: UpgradeType
    var name: String

    constructor(type: UpgradeType, name: String?) {
        this.type = type
        this.name = name ?: when (type) {
            else -> type.toString()
        }
    }
}