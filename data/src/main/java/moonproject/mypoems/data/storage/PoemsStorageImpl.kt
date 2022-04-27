package moonproject.mypoems.data.storage

import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import moonproject.mypoems.data.models.PoemDataRealm
import moonproject.mypoems.data.models.PoemFieldRealm

class PoemsStorageImpl(
    private val realm: Realm
) : PoemsStorage {

    override fun getAllPoems(): Flow<List<PoemFieldRealm>> {
        return realm.where<PoemFieldRealm>().findAll().toFlow()
    }

    override fun getPoemById(id: Int): PoemFieldRealm {
        return PoemFieldRealm(0, "f", RealmList(
            PoemDataRealm("sfd", "", "hello", "", 0L)
        ))
    }

    override fun saveNewPoem(poem: PoemFieldRealm): Flow<String> {
        return flow {
            emit("Trying")
            realm.executeTransactionAsync ({
                it.copyToRealm(poem)
            }, {
                suspend { emit("Success") }
            }, {
                suspend { emit("Error: ${it.message}") }
            })
        }
    }

    override fun updatePoem(id: Int, poemDataRealm: PoemDataRealm) {
    }

}