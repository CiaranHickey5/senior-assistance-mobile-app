package ie.setu.initial_implementation

import android.app.Application
import ie.setu.initial_implementation.data.database.AppDatabase

class DailyHavenApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}