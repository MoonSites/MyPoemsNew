package moonproject.mypoems.updated

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import io.realm.FieldAttribute
import io.realm.Realm
import io.realm.RealmConfiguration
import moonproject.mypoems.data.storage.UserPreferences
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.updated.di.appModule
import moonproject.mypoems.updated.di.dataModule
import moonproject.mypoems.updated.di.domainModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
            .name("poems.realm")
            .schemaVersion(2L)
            .migration { realm, _, newVersion ->
                when (newVersion) {
                    2L -> {
                        realm.schema
                            .get("PoemDataRealm")!!
                            .addField(PoemData::legacyDate.name, String::class.java, FieldAttribute.REQUIRED)
                    }
                }
            }
            .build()

        Realm.setDefaultConfiguration(realmConfig)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, domainModule, dataModule)
        }

        val prefs = get<UserPreferences>()
        val mode = when (prefs.useDarkTheme) {
            true  -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
            null  -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        DynamicColors.applyToActivitiesIfAvailable(this) { _, _ ->
            prefs.useDynamicColors
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

}