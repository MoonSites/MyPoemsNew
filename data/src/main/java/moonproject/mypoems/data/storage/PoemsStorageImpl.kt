package moonproject.mypoems.data.storage

import android.content.Context
import androidx.core.content.edit
import io.realm.Case
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun getAllPoems(sort: Sort, filterFieldName: String, filterFieldValue: String): Flow<List<PoemField>> {
        val query = realm.where<PoemFieldRealm>()
            .sort(PoemFieldRealm::id.name, sort)

        if (filterFieldValue.isNotEmpty()) {
            query.contains(filterFieldName, filterFieldValue, Case.INSENSITIVE)
        }

        return query.findAll().toFlow()
    }

    override fun getPoemById(id: Long): Flow<PoemField?> = callbackFlow {
        val observable = realm.where<PoemFieldRealm>()
            .equalTo("id", id)
            .findFirstAsync()

        observable.addChangeListener { obj: PoemFieldRealm?, _ ->
            obj?.removeAllChangeListeners()
            if (obj?.isValid == true) {
                trySend(obj)
            } else {
                trySend(null)
            }
        }

        awaitClose { observable.removeAllChangeListeners() }
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