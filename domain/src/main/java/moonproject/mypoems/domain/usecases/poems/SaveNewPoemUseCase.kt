package moonproject.mypoems.domain.usecases.poems

import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.repo.PoemsRepo

class SaveNewPoemUseCase(
    private val poemsRepo: PoemsRepo
) {

    operator fun invoke(poem: PoemField) = poemsRepo.saveNewPoem(poem = poem)

}