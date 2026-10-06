package myfirst.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey val id: Int = 1,
    val money: Double = 0.0,
    val basePerClick: Double = 1.0,
    val multiplier: Double = 1.0,
    val cooldownMs: Long = 500L,
    val critChance: Float = 0.05f,          // 5%
    val longPressMultiplier: Float = 3f,    // x3
    val addPerClickLevel: Int = 0,
    val multiplyPerClickLevel: Int = 0,
    val reduceCooldownLevel: Int = 0,
    val critChanceLevel: Int = 0,
    val longPressLevel: Int = 0
)
