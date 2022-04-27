package moonproject.mypoems.data.storage

import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import moonproject.mypoems.data.models.PoemFieldRealm
import moonproject.mypoems.data.models.PoemsToRealmMapper
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

class PoemsStorageImpl(
    private val realm: Realm,
    private val realmMapper: PoemsToRealmMapper
) : PoemsStorage {

    override fun getAllPoems(): Flow<List<PoemField>> {
        return realm.where<PoemFieldRealm>().findAll().toFlow()
    }

    override fun getPoemById(id: Int): PoemField {
        return PoemFieldRealm(0, "f", RealmList())
    }

    override fun saveNewPoem(poem: PoemField): Flow<String> = callbackFlow {
        trySend("Trying")
        val realmTask = realm.executeTransactionAsync (
            { it.copyToRealm(realmMapper.mapToRealm(poem)) },
            { trySend("Success"); close() },
            { cancel("PoemsStorageImpl.saveNewPoem Error", it) }
        )
        awaitClose { realmTask.cancel() }
    }

    override fun updatePoem(id: Int, poemData: PoemData) {
    }

}