package myfirst.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Одна строка в таблице — текущее состояние игры.
 * id всегда = 1.
 */
@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey val id: Int = 1,
    val money: Float = 0f,
    val basePerClick: Float = 1f,
    val multiplier: Float = 1f,
    val cooldownMs: Long = 500L,
    val addPerClickLevel: Int = 0,
    val multiplyPerClickLevel: Int = 0,
    val reduceCooldownLevel: Int = 0
)
