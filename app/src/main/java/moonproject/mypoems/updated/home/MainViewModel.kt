package moonproject.mypoems.updated.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import moonproject.mypoems.domain.usecases.poems.GetCurrentPoemUseCase
import moonproject.mypoems.domain.usecases.poems.GetSortedPoemsUseCase
import moonproject.mypoems.domain.usecases.poems.UpdatePoemUseCase
import moonproject.mypoems.updated.models.AdapterPoem
import moonproject.mypoems.updated.models.AdapterPoemMapper

class MainViewModel(
    private val getSortedPoemsUseCase: GetSortedPoemsUseCase,
    private val getCurrentPoemUseCase: GetCurrentPoemUseCase,
    private val updatePoemUseCase: UpdatePoemUseCase,
    private val adapterPoemMapper: AdapterPoemMapper,
) : ViewModel() {

//    private val _poemsList = MutableLiveData<List<AdapterPoem>>()
    val poemsList: LiveData<List<AdapterPoem>> =
        getSortedPoemsUseCase("")
            .map { list ->
                list.map { poemField -> adapterPoemMapper.map(poemField) }
            }
            .asLiveData()

//    private val _currentPoem = MutableLiveData<PoemField>()
//    val currentPoem: LiveData<PoemField> = _currentPoem

//    fun a () {
//        _poemsList.value = getSortedPoemsUseCase("").map { AdapterPoem(it.poems[0].title, "") }
//    }

    fun initPoemsList() {
//        _poemsList.value =
    }


}