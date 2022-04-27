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

    override fun getPoemById(id: Int): PoemField = poemsStorage.getPoemById(id)

    override fun saveNewPoem(poem: PoemField) = poemsStorage.saveNewPoem(poem)

    override fun updatePoem(id: Int, poemData: PoemData) = poemsStorage.updatePoem(
        id = id,
        poemData = poemData
    )


    private fun sortMap(sorting: GetPoemsParams.Sorting): Sort = when(sorting) {
        GetPoemsParams.Sorting.ASCENDING  -> Sort.ASCENDING
        GetPoemsParams.Sorting.DESCENDING -> Sort.DESCENDING
    }

}