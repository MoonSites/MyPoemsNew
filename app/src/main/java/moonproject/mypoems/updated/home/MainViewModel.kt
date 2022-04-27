package moonproject.mypoems.updated.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.usecases.poems.GetCurrentPoemUseCase
import moonproject.mypoems.domain.usecases.poems.GetSortedPoemsUseCase
import moonproject.mypoems.domain.usecases.poems.UpdatePoemUseCase
import moonproject.mypoems.updated.models.AdapterPoem
import moonproject.mypoems.updated.models.DomainToPresenterPoemMapper

class MainViewModel(
    getSortedPoemsUseCase: GetSortedPoemsUseCase,
    private val getCurrentPoemUseCase: GetCurrentPoemUseCase,
    private val updatePoemUseCase: UpdatePoemUseCase,
    private val domainToPresenterPoemMapper: DomainToPresenterPoemMapper,
) : ViewModel() {


    private val poemsSorting = MutableStateFlow(GetPoemsParams.Sorting.ASCENDING)
    val poemsFilterField = MutableStateFlow(GetPoemsParams.FilterField.Title)
    val poemsFilterText  = MutableStateFlow("")


    private val getPoemsParams = combine(
        poemsSorting,
        poemsFilterField,
        poemsFilterText
    ) { a, b, c ->
       GetPoemsParams(a, b, c)
    }

    val poemsList: StateFlow<List<AdapterPoem>> = getPoemsParams.flatMapLatest { params ->
        getSortedPoemsUseCase(params)
            .map { list: List<PoemField> ->
                list.map { poemField -> domainToPresenterPoemMapper.map(poemField) }
            }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(1000),
        initialValue = listOf()
    )


    fun toggleSorting() {
        poemsSorting.value =
            if (poemsSorting.value == GetPoemsParams.Sorting.ASCENDING) {
                GetPoemsParams.Sorting.DESCENDING
            } else {
                GetPoemsParams.Sorting.ASCENDING
            }
    }

    fun toggleFilterField() {
        poemsFilterField.value =
            if (poemsFilterField.value == GetPoemsParams.FilterField.Title) {
                GetPoemsParams.FilterField.FirstLine
            } else {
                GetPoemsParams.FilterField.Title
            }
    }

//    private val _currentPoem = MutableLiveData<PoemField>()
//    val currentPoem: LiveData<PoemField> = _currentPoem

}