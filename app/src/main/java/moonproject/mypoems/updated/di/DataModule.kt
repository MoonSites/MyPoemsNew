package moonproject.mypoems.updated.di

import io.realm.Realm
import moonproject.mypoems.data.PasswordRepoImpl
import moonproject.mypoems.data.PoemsRepoImpl
import moonproject.mypoems.data.models.PasswordDataToEncodedMapper
import moonproject.mypoems.data.models.PasswordEncodedToDataMapper
import moonproject.mypoems.data.models.PoemsToRealmMapper
import moonproject.mypoems.data.storage.PasswordStorage
import moonproject.mypoems.data.storage.PoemsStorage
import moonproject.mypoems.data.storage.PoemsStorageImpl
import moonproject.mypoems.data.storage.SharedPrefPasswordStorage
import moonproject.mypoems.domain.repo.PasswordRepo
import moonproject.mypoems.domain.repo.PoemsRepo
import org.koin.dsl.module
import org.koin.dsl.onClose

val dataModule = module {

    single<Realm> {
        Realm.getDefaultInstance()
    }.onClose {
        if (it?.isClosed == false) {
            it.close()
        }
    }


    single<PasswordStorage> {
        SharedPrefPasswordStorage(context = get())
    }

    single<PasswordRepo> {
        PasswordRepoImpl(
            passwordStorage = get(),
            encodedToDataMapper = PasswordEncodedToDataMapper(),
            dataToEncodedMapper = PasswordDataToEncodedMapper()
        )
    }


    single<PoemsStorage> {
        PoemsStorageImpl(
            realm = get(),
            realmMapper = PoemsToRealmMapper())
    }

    single<PoemsRepo> {
        PoemsRepoImpl(
            poemsStorage = get(),
//            poemsMapper = PoemsMapper()
        )
    }

}