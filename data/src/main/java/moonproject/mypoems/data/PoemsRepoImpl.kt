package moonproject.mypoems.data

import io.realm.Sort
import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.data.storage.PoemsStorage
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class PoemsRepoImpl(
    private val poemsStorage: PoemsStorage,
//    private val poemsMapper: PoemsMapper
) : PoemsRepo {

    override fun getAllPoems(params: GetPoemsParams): Flow<List<PoemField>> = poemsStorage.getAllPoems(
        sortMap(params.sorting),
        params.filterField.fieldName,
        params.filterText
    )

    override fun getPoemById(id: Long): Flow<PoemField?> = poemsStorage.getPoemById(id)

    override fun saveNewPoem(poem: PoemField) = poemsStorage.saveNewPoem(poem)

    override fun updatePoem(poem: PoemField, poemData: PoemData) = poemsStorage.updatePoem(
        poem = poem,
        newPoemData = poemData
    )

    override fun deletePoem(id: Long): Flow<Boolean> = poemsStorage.deletePoem(id)

    override fun getSearchPoemsParams(): GetPoemsParams = poemsStorage.getSearchPoemsParams()

    override fun saveSearchPoemsParams(params: GetPoemsParams) = poemsStorage.saveSearchPoemsParams(params)


    private fun sortMap(sorting: GetPoemsParams.Sorting): Sort = when(sorting) {
        GetPoemsParams.Sorting.ASCENDING  -> Sort.ASCENDING
        GetPoemsParams.Sorting.DESCENDING -> Sort.DESCENDING
    }

}