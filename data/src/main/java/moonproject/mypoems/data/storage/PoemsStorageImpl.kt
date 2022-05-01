package moonproject.mypoems.data.storage

import android.content.Context
import androidx.core.content.edit
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import moonproject.mypoems.data.models.PoemFieldRealm
import moonproject.mypoems.data.models.PoemsToRealmMapper
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

class PoemsStorageImpl(
    context: Context,
    private val realm: Realm,
    private val realmMapper: PoemsToRealmMapper
) : PoemsStorage {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getAllPoems(sort: Sort, filterField: GetPoemsParams.FilterField, filterFieldValue: String): Flow<List<PoemField>> {
        val query = realm.where<PoemFieldRealm>()
            .sort(PoemFieldRealm::id.name, sort)

//        потому что Case.INSENSITIVE поддерживает только латинский алфавит //мда
//        if (filterFieldValue.isNotEmpty()) {
//            query
//                .contains(filterFieldName, filterFieldValue, Case.INSENSITIVE)
//                .or()
//                .contains(filterFieldName, filterFieldValue.lowercase(Locale.getDefault()), Case.INSENSITIVE)
//        }

        return query.findAll().toFlow().map { results ->
            val out = arrayListOf<PoemField>()

            results.forEach { poemFieldRealm ->
                val field =
                    when (filterField) {
                        GetPoemsParams.FilterField.Title -> poemFieldRealm.currentTitle
                        GetPoemsParams.FilterField.FirstLine -> poemFieldRealm.currentFirstLine
                    }

                if (field.contains(filterFieldValue, true)) {
                    out.add(poemFieldRealm)
                }
            }
            out
        }
    }

    override fun getPoemById(id: Long): Flow<PoemField?> /*= callbackFlow*/ {
        val observable = realm.where<PoemFieldRealm>()
            .equalTo(PoemFieldRealm::id.name, id)
            .findFirstAsync()

        return observable.toFlow().map {
            if (it == null || !it.isValid) null
            else it
        }
//        observable.addChangeListener { obj: PoemFieldRealm?, _ ->
//            obj?.removeAllChangeListeners()
//            if (obj?.isValid == true) {
//                trySend(obj)
//            } else {
//                trySend(null)
//            }
//        }
//
//        awaitClose { observable.removeAllChangeListeners() }
    }

    override fun saveNewPoem(poem: PoemField): Flow<Boolean> = callbackFlow {
        val realmTask = realm.executeTransactionAsync (
            { it.insert(realmMapper.mapToRealm(poem)) },
            { trySend(true);  close() },
            { trySend(false); cancel("PoemsStorageImpl.saveNewPoem Error", it) }
        )
        awaitClose { realmTask.cancel() }
    }

    override fun updatePoem(poem: PoemField, newPoemData: PoemData?): Flow<Boolean> = callbackFlow {
        val onSuccess: () -> Unit = {
            trySend(true)
            close()
        }
        val onError: (Throwable) -> Unit = { t: Throwable ->
            trySend(false)
            cancel("PoemsStorageImpl.updatePoem Error", t)
        }

        val transaction: (Realm) -> Unit = transaction@ { it: Realm ->
            val realmPoem = it.where<PoemFieldRealm>()
                .equalTo(PoemFieldRealm::id.name, poem.id)
                .findFirst()

            if (realmPoem == null) {
                onError( NullPointerException("transaction realmPoem(id = ${poem.id}) is null! ") )
                return@transaction
            }

            realmPoem.author           = poem.author
            realmPoem.currentTitle     = poem.currentTitle
            realmPoem.currentFirstLine = poem.currentFirstLine

            if (newPoemData != null) {
                realmPoem.poemsRealm.add(
                    realmMapper.mapPoemDataToRealm(newPoemData)
                )
            }
        }
        val realmTask = realm.executeTransactionAsync(transaction, onSuccess, onError)
        awaitClose { realmTask.cancel() }
    }

    override fun deletePoem(id: Long): Flow<Boolean> = callbackFlow {
        val onSuccess: () -> Unit = {
            trySend(true)
            close()
        }
        val onError: (Throwable) -> Unit = { t: Throwable ->
            trySend(false)
            cancel("PoemsStorageImpl.deletePoem Error", t)
        }

        val transaction: (Realm) -> Unit = transaction@{ it: Realm ->
            val realmPoem = it.where<PoemFieldRealm>()
                .equalTo(PoemFieldRealm::id.name, id)
                .findFirst()

            if (realmPoem == null) {
                onError(NullPointerException("transaction realmPoem(id = ${id}) is null! "))
                return@transaction
            }

            realmPoem.poemsRealm.deleteAllFromRealm()
            realmPoem.deleteFromRealm()
        }

        val realmTask = realm.executeTransactionAsync(transaction, onSuccess, onError)
        awaitClose { realmTask.cancel() }
    }


    override fun getSearchPoemsParams(): GetPoemsParams {
        val sortingPrefs = prefs.getString(KEY_SORTING, GetPoemsParams.Sorting.defaultValue)
            ?: GetPoemsParams.Sorting.defaultValue

        val filterFieldPrefs = prefs.getString(KEY_FILTER_FIELD, GetPoemsParams.FilterField.defaultValue)
            ?: GetPoemsParams.FilterField.defaultValue

        var sorting     = GetPoemsParams.Sorting.valueOf(GetPoemsParams.Sorting.defaultValue)
        var filterField = GetPoemsParams.FilterField.valueOf(GetPoemsParams.FilterField.defaultValue)

        try {
            sorting     = GetPoemsParams.Sorting.valueOf(sortingPrefs)
            filterField = GetPoemsParams.FilterField.valueOf(filterFieldPrefs)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        return GetPoemsParams(sorting, filterField)
    }

    override fun saveSearchPoemsParams(params: GetPoemsParams) {
        prefs.edit {
            putString(KEY_SORTING, params.sorting.name)
            putString(KEY_FILTER_FIELD, params.filterField.name)
        }
    }


    companion object {
        const val PREFS_NAME = "poems_prefs"
        const val KEY_SORTING = "KEY_SORTING"
        const val KEY_FILTER_FIELD = "KEY_FILTER_FIELD"
    }

}