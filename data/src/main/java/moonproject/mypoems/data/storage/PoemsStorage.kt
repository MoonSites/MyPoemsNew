package moonproject.mypoems.data.storage

import io.realm.Sort
import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

interface PoemsStorage {

    fun getAllPoems(sort: Sort, filterFieldName: String, filterFieldValue: String): Flow<List<PoemField>>
    fun getPoemById(id: Long): Flow<PoemField?>
    fun saveNewPoem(poem: PoemField): Flow<String>
    fun updatePoem(id: Int, poemData: PoemData)

    fun getSearchPoemsParams(): GetPoemsParams
    fun saveSearchPoemsParams(params: GetPoemsParams)

}