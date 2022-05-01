package moonproject.mypoems.domain.repo

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

interface PoemsRepo {

    fun getAllPoems(params: GetPoemsParams): Flow<List<PoemField>>
    fun getPoemById(id: Long): Flow<PoemField?>
    fun saveNewPoem(poem: PoemField): Flow<Boolean>
    fun updatePoem(poem: PoemField, poemData: PoemData?): Flow<Boolean>
    fun deletePoem(id: Long): Flow<Boolean>

    fun getSearchPoemsParams(): GetPoemsParams
    fun saveSearchPoemsParams(params: GetPoemsParams)
}