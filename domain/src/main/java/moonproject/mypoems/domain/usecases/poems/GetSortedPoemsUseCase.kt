package moonproject.mypoems.domain.usecases.poems

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class GetSortedPoemsUseCase(
    private val poemsRepo: PoemsRepo
) {

    operator fun invoke(sorting: Any): Flow<List<PoemField>> {
        return poemsRepo.getAllPoems()
    }

}