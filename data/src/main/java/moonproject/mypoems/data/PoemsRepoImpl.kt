package moonproject.mypoems.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import moonproject.mypoems.data.models.PoemsMapper
import moonproject.mypoems.data.storage.PoemsStorage
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class PoemsRepoImpl(
    private val poemsStorage: PoemsStorage,
    private val poemsMapper: PoemsMapper
) : PoemsRepo {

    override fun getAllPoems(): Flow<List<PoemField>> = poemsStorage.getAllPoems()
        .map { list ->
            list.map { poemsMapper.mapFieldToDomain(it) }
        }

    override fun getPoemById(id: Int): PoemField = poemsMapper.mapFieldToDomain(obj = poemsStorage.getPoemById(id))

    override fun saveNewPoem(poem: PoemField) = poemsStorage.saveNewPoem(poemsMapper.mapFieldToRealm(poem))

    override fun updatePoem(id: Int, poemData: PoemData) = poemsStorage.updatePoem(
        id = id,
        poemDataRealm = poemsMapper.mapDataToRealm(poemData)
    )

}