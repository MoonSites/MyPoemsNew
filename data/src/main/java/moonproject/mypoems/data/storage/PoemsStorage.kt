package moonproject.mypoems.data.storage

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

interface PoemsStorage {

    fun getAllPoems(): Flow<List<PoemField>>
    fun getPoemById(id: Int) : PoemField
    fun saveNewPoem(poem: PoemField): Flow<String>
    fun updatePoem(id: Int, poemData: PoemData)
}