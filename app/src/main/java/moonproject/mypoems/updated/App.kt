package moonproject.mypoems.updated

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.realm.Realm
import io.realm.RealmConfiguration
import moonproject.mypoems.updated.di.appModule
import moonproject.mypoems.updated.di.dataModule
import moonproject.mypoems.updated.di.domainModule
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
            .schemaVersion(1L)
//            .initialData { realm ->
//                val data = listOf(
//                    PoemFieldRealm(1, "f"),
//                    PoemFieldRealm(2, "fsdf"),
//                    PoemFieldRealm(3, "ffgf"),
//                )
//                data[0].poemsRealm = RealmList(PoemDataRealm("sfd", "", "hello", "", 0L))
//                data[1].poemsRealm = RealmList(PoemDataRealm("dsfgsfdfg", "", "hellohellohellohellohellohellohellohellohellohellohellohellohello", "", 0L))
//                data[2].poemsRealm = RealmList(PoemDataRealm("dsfgsfdfg", "", "my name is\nsdfsdfssdfsdfssdfsdfs", "", 0L))
//
//                realm.copyToRealm(data)
//            }
            .build()
        Realm.setDefaultConfiguration(realmConfig)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, domainModule, dataModule)
        }
//        DynamicColors.applyToActivitiesIfAvailable(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}