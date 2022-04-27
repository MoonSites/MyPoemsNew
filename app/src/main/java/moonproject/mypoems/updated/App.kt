package moonproject.mypoems.updated

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import moonproject.mypoems.data.models.PoemDataRealm
import moonproject.mypoems.data.models.PoemFieldRealm
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
            .initialData { realm ->
                val data = listOf(
                    PoemFieldRealm(1, "f", RealmList(
                        PoemDataRealm("sfd", "", "hello", "", 0L)
                    )
                    ),
                    PoemFieldRealm(2, "fsdf", RealmList(
                        PoemDataRealm("dsfgsfdfg", "", "hellohellohellohellohellohellohellohellohellohellohellohellohello", "", 0L)
                    )
                    ),
                    PoemFieldRealm(3, "ffgf", RealmList(
                        PoemDataRealm("dsfgsfdfg", "", "my name is\nsdfsdfssdfsdfssdfsdfs", "", 0L)
                    )
                    ),
                )
                realm.copyToRealm(data)
            }
            .build()
        Realm.setDefaultConfiguration(realmConfig)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, domainModule, dataModule)
        }
//        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}