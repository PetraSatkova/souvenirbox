package cz.mendelu.souvenirbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [SouvenirEntity::class], version = 6, exportSchema = true)
abstract class SouvenirsDatabase : RoomDatabase() {

    abstract fun souvenirsDao(): SouvenirsDao

    companion object {

        private var INSTANCE: SouvenirsDatabase? = null

        fun getDatabase(context: Context): SouvenirsDatabase {
            if (INSTANCE == null) {
                synchronized(SouvenirsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            SouvenirsDatabase::class.java,
                            "souvenirs_database"
                        ).fallbackToDestructiveMigration(false).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}