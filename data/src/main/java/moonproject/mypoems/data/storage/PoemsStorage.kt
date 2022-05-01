package moonproject.mypoems.data.storage

import io.realm.Sort
import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

interface PoemsStorage {

    fun getAllPoems(sort: Sort, filterField: GetPoemsParams.FilterField, filterFieldValue: String): Flow<List<PoemField>>
    fun getPoemById(id: Long): Flow<PoemField?>
    fun saveNewPoem(poem: PoemField): Flow<Boolean>
    fun updatePoem(poem: PoemField, newPoemData: PoemData?): Flow<Boolean>
    fun deletePoem(id: Long): Flow<Boolean>

    fun getSearchPoemsParams(): GetPoemsParams
    fun saveSearchPoemsParams(params: GetPoemsParams)

}