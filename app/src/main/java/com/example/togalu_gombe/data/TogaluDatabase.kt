package com.example.togalu_gombe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Play::class, Scene::class, Puppet::class, HistoryVideo::class, StoreItem::class], version = 1, exportSchema = false)
abstract class TogaluDatabase : RoomDatabase() {

    abstract fun togaluDao(): TogaluDao

    companion object {
        @Volatile
        private var INSTANCE: TogaluDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TogaluDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TogaluDatabase::class.java,
                    "togalu_database"
                )
                .addCallback(TogaluDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TogaluDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.togaluDao())
                }
            }
        }

        suspend fun populateDatabase(dao: TogaluDao) {
            // Add mock data
            val play = Play(1, "Ramayana", "ರಾಮಾಯಣ", "The epic tale of Lord Rama.", "ಭಗವಾನ್ ರಾಮನ ಮಹಾಕಾವ್ಯ.")
            dao.insertPlays(listOf(play))

            val scenes = listOf(
                Scene(playId = 1, sceneNumber = 1, titleEn = "Birth of Rama", titleKn = "ರಾಮನ ಜನನ", summaryEn = "King Dasharatha performs a yagna.", summaryKn = "ದಶರಥ ಮಹಾರಾಜನು ಯಜ್ಞ ಮಾಡುತ್ತಾನೆ.", characters = "Dasharatha, Rama"),
                Scene(playId = 1, sceneNumber = 2, titleEn = "Sita Swayamvara", titleKn = "ಸೀತಾ ಸ್ವಯಂವರ", summaryEn = "Rama breaks the Shiva Dhanush.", summaryKn = "ರಾಮನು ಶಿವಧನುಸ್ಸನ್ನು ಮುರಿಯುತ್ತಾನೆ.", characters = "Rama, Sita, Janaka"),
                Scene(playId = 1, sceneNumber = 3, titleEn = "Exile", titleKn = "ವನವಾಸ", summaryEn = "Rama goes to the forest for 14 years.", summaryKn = "ರಾಮನು 14 ವರ್ಷಗಳ ಕಾಲ ಕಾಡಿಗೆ ಹೋಗುತ್ತಾನೆ.", characters = "Rama, Sita, Lakshmana")
            )
            dao.insertScenes(scenes)

            val puppets = listOf(
                Puppet("p1", "Hanuman", "ಹನುಮಂತ", "Super strength, flight", "ಅತಿಮಾನುಷ ಶಕ್ತಿ, ಹಾರಾಟ", "Devotion and loyalty", "ಭಕ್ತಿ ಮತ್ತು ನಿಷ್ಠೆ", 0),
                Puppet("p2", "Ravana", "ರಾವಣ", "Ten heads, master of weapons", "ಹತ್ತು ತಲೆಗಳು, ಶಸ್ತ್ರಾಸ್ತ್ರಗಳ ಒಡೆಯ", "Ego and intellect", "ಅಹಂಕಾರ ಮತ್ತು ಬುದ್ಧಿ", 0)
            )
            dao.insertPuppets(puppets)
            
            val historyVideos = listOf(
                HistoryVideo(1, "Making of Leather Puppets", "ತೊಗಲು ಗೊಂಬೆಗಳ ತಯಾರಿಕೆ", "android.resource://com.example.togalu_gombe/raw/history_video_1", 0)
            )
            dao.insertHistoryVideos(historyVideos)
            
            val storeItems = listOf(
                StoreItem(1, "Hanuman Miniature", "ಹನುಮಂತನ ಕಿರು ಗೊಂಬೆ", 500.0, "", "Miniature"),
                StoreItem(2, "Weekend Workshop", "ವಾರಾಂತ್ಯದ ಕಾರ್ಯಾಗಾರ", 1500.0, "", "Workshop")
            )
            dao.insertStoreItems(storeItems)
        }
    }
}
