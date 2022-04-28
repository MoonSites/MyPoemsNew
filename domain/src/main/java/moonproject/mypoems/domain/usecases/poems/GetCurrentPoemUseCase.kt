package moonproject.mypoems.domain.usecases.poems

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class GetCurrentPoemUseCase(
    private val poemsRepo: PoemsRepo
) {

    operator fun invoke(id: Long): Flow<PoemField?> = poemsRepo.getPoemById(id)

}