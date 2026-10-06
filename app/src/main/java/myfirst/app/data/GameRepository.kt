package myfirst.app.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameDao) {

    fun observeState(): Flow<GameStateEntity?> = dao.observeState()

    suspend fun getState(): GameStateEntity =
        dao.getState() ?: GameStateEntity()

    suspend fun save(
        money: Float,
        basePerClick: Float,
        multiplier: Float,
        cooldownMs: Long,
        addPerClickLevel: Int,
        multiplyPerClickLevel: Int,
        reduceCooldownLevel: Int
    ) {
        dao.saveState(
            GameStateEntity(
                id = 1,
                money = money,
                basePerClick = basePerClick,
                multiplier = multiplier,
                cooldownMs = cooldownMs,
                addPerClickLevel = addPerClickLevel,
                multiplyPerClickLevel = multiplyPerClickLevel,
                reduceCooldownLevel = reduceCooldownLevel
            )
        )
    }
}
