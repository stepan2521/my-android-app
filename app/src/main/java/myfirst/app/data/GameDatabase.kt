package myfirst.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [GameStateEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        /**
         * Миграция 1 → 2:
         * - money / basePerClick / multiplier остаются REAL (Float → Double в SQLite одно и то же)
         * - добавляем critChance, longPressMultiplier и уровни новых прокачек
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE game_state ADD COLUMN critChance REAL NOT NULL DEFAULT 0.05"
                )
                db.execSQL(
                    "ALTER TABLE game_state ADD COLUMN longPressMultiplier REAL NOT NULL DEFAULT 3.0"
                )
                db.execSQL(
                    "ALTER TABLE game_state ADD COLUMN critChanceLevel INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE game_state ADD COLUMN longPressLevel INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getInstance(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
