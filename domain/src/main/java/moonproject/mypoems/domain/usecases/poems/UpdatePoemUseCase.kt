package moonproject.mypoems.domain.usecases.poems

import kotlinx.coroutines.flow.Flow
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class UpdatePoemUseCase(
    private val poemsRepo: PoemsRepo
) {

    operator fun invoke(poemField: PoemField, poemData: PoemData?): Flow<Boolean> {
        return poemsRepo.updatePoem(poemField, poemData)
    }

}