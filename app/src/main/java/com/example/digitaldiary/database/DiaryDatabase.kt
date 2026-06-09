package com.example.digitaldiary.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String? = null,
    val moodEmoji: String = "😊",
    val dayRating: Int = 5
)

@Entity(tableName = "unlocked_plants")
data class UnlockedPlant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantTier: Int,
    val unlockDate: Long,
    val isViewed: Boolean = false
)

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<DiaryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DiaryEntry)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedPlant(plant: UnlockedPlant)

    @Query("SELECT * FROM unlocked_plants ORDER BY plantTier ASC")
    fun getAllUnlockedPlants(): Flow<List<UnlockedPlant>>

    // new
    @Query("DELETE FROM unlocked_plants")
    suspend fun clearAllUnlockedPlants()
}

// BUMPED VERSION FROM 3 TO 4 TO SUPPORT EXPANDED WEEKS LOGIC
@Database(entities = [DiaryEntry::class, UnlockedPlant::class], version = 4)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE diary_entries ADD COLUMN moodEmoji TEXT NOT NULL DEFAULT '😊'")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `unlocked_plants` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `plantTier` INTEGER NOT NULL, `unlockDate` INTEGER NOT NULL, `isViewed` INTEGER NOT NULL)")
            }
        }

        // NEW: Blank migration for schema stability during version bump
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // No SQL table schema changes required since plantTier handles 1-8 natively
            }
        }

        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}