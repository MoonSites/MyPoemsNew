package moonproject.mypoems.updated.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.usecases.poems.GetCurrentPoemUseCase
import moonproject.mypoems.domain.usecases.poems.GetSortedPoemsUseCase
import moonproject.mypoems.domain.usecases.poems.SearchPoemsParamsUseCase
import moonproject.mypoems.domain.usecases.poems.UpdatePoemUseCase
import moonproject.mypoems.updated.models.AdapterPoem
import moonproject.mypoems.updated.models.DomainToPresenterPoemMapper

class MainViewModel(
    getSortedPoemsUseCase: GetSortedPoemsUseCase,
    private val searchPoemsParamsUseCase: SearchPoemsParamsUseCase,
    private val getCurrentPoemUseCase: GetCurrentPoemUseCase,
    private val updatePoemUseCase: UpdatePoemUseCase,
    private val domainToPresenterPoemMapper: DomainToPresenterPoemMapper,
) : ViewModel() {

    private val poemsFilterText  = MutableStateFlow("")
    private val poemsFilterField = MutableStateFlow(GetPoemsParams.FilterField.Title)
    private val poemsSorting     = MutableStateFlow(GetPoemsParams.Sorting.ASCENDING)

    private val getPoemsParams = combine(
        poemsSorting,
        poemsFilterField,
        poemsFilterText
    ) { a, b, c ->
       GetPoemsParams(a, b, c)
    }

    val poemsList: StateFlow<List<AdapterPoem>> = getPoemsParams.flatMapLatest { params ->
        searchPoemsParamsUseCase.save(params)

        getSortedPoemsUseCase(params)
            .map { list: List<PoemField> ->
                list.map { poemField -> domainToPresenterPoemMapper.map(poemField) }
            }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(1000),
        initialValue = listOf()
    )


    init {
        searchPoemsParamsUseCase.get().let {
            poemsSorting.value = it.sorting
            poemsFilterField.value = it.filterField
        }
    }


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

    fun setFilterText(text: String) {
        poemsFilterText.value = text
    }

    fun openCurrentPoem(id: Long) {
        _currentPoemId.value = id
    }

    fun closeCurrentPoem() {
        _currentPoemId.value = 0L
    }
    fun isPoemOpened() = _currentPoemId.value != 0L

    private val _currentPoemId = MutableStateFlow<Long>(0)
    val currentPoemId = _currentPoemId.asStateFlow()

    val currentPoem: Flow<PoemField?> = _currentPoemId
        .filter { it != 0L }
        .flatMapLatest { id -> getCurrentPoemUseCase(id) }
//        .map { it }       //maybe I should map it to presentation-layer object, because domain-object has vars (but it's better if they will be vals)

}