package moonproject.mypoems.data.storage

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.data.models.PoemDataRealm
import moonproject.mypoems.data.models.PoemFieldRealm

interface PoemsStorage {

    fun getAllPoems(): Flow<List<PoemFieldRealm>>
    fun getPoemById(id: Int) : PoemFieldRealm
    fun saveNewPoem(poem: PoemFieldRealm): Flow<String>
    fun updatePoem(id: Int, poemDataRealm: PoemDataRealm)
}