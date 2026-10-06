package myfirst.app.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameDao) {

    fun observeState(): Flow<GameStateEntity?> = dao.observeState()

    suspend fun getState(): GameStateEntity =
        dao.getState() ?: GameStateEntity()

    suspend fun save(state: GameStateEntity) {
        dao.saveState(state.copy(id = 1))
    }
}
