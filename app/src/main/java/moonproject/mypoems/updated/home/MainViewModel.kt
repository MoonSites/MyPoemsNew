package moonproject.mypoems.updated.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.usecases.poems.GetCurrentPoemUseCase
import moonproject.mypoems.domain.usecases.poems.GetSortedPoemsUseCase
import moonproject.mypoems.domain.usecases.poems.UpdatePoemUseCase
import moonproject.mypoems.updated.models.AdapterPoem
import moonproject.mypoems.updated.models.AdapterPoemMapper

class MainViewModel(
    getSortedPoemsUseCase: GetSortedPoemsUseCase,
    private val getCurrentPoemUseCase: GetCurrentPoemUseCase,
    private val updatePoemUseCase: UpdatePoemUseCase,
    private val adapterPoemMapper: AdapterPoemMapper,
) : ViewModel() {

    val poemsList: LiveData<List<AdapterPoem>> =
        getSortedPoemsUseCase("")
            .map { list: List<PoemField> ->
                list.map { poemField -> adapterPoemMapper.map(poemField) }
            }
            .asLiveData()

//    private val _currentPoem = MutableLiveData<PoemField>()
//    val currentPoem: LiveData<PoemField> = _currentPoem

}