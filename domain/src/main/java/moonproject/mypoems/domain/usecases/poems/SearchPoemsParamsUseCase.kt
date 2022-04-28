package moonproject.mypoems.domain.usecases.poems

import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.repo.PoemsRepo

class SearchPoemsParamsUseCase(
    private val poemsRepo: PoemsRepo
) {

    private var lastSavedGetPoemsParams: GetPoemsParams? = null

    fun get(): GetPoemsParams {
        return poemsRepo.getSearchPoemsParams().also { lastSavedGetPoemsParams = it }
    }

    fun save(params: GetPoemsParams) {
        if (params.filterField == lastSavedGetPoemsParams?.filterField
            && params.sorting == lastSavedGetPoemsParams?.sorting) {
            return
        }
        lastSavedGetPoemsParams = params
        poemsRepo.saveSearchPoemsParams(params)
    }

}