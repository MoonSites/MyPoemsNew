package moonproject.mypoems.domain.usecases.poems

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.repo.PoemsRepo

class DeletePoemUseCase(
    private val poemsRepo: PoemsRepo
) {

    operator fun invoke(id: Long): Flow<Boolean> {
        return poemsRepo.deletePoem(id)
    }

}