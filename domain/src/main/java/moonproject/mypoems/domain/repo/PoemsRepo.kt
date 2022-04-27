package moonproject.mypoems.domain.repo

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

interface PoemsRepo {

    fun getAllPoems(params: GetPoemsParams): Flow<List<PoemField>>
    fun getPoemById(id: Int): PoemField
    fun saveNewPoem(poem: PoemField): Flow<String>
    fun updatePoem(id: Int, poemData: PoemData)

}