package moonproject.mypoems.updated.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.usecases.poems.*
import moonproject.mypoems.updated.extensions.log
import moonproject.mypoems.updated.models.AdapterPoem
import moonproject.mypoems.updated.models.DomainToPresenterPoemMapper
import moonproject.mypoems.updated.models.SavePoemDataParam
import moonproject.mypoems.updated.models.SavePoemFieldParam

class MainViewModel(
    getSortedPoemsUseCase: GetSortedPoemsUseCase,
    private val searchPoemsParamsUseCase: SearchPoemsParamsUseCase,
    private val getCurrentPoemUseCase: GetCurrentPoemUseCase,
    private val updatePoemUseCase: UpdatePoemUseCase,
    private val deletePoemUseCase: DeletePoemUseCase,
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

    private val _currentPoemId = MutableStateFlow<Long>(0)
    val currentPoemId = _currentPoemId.asStateFlow()

    val currentPoem = _currentPoemId
        .filter { it != 0L }
        .flatMapLatest { id -> getCurrentPoemUseCase(id) }
        .onEach { log("VM currentPoem", it) }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(1000),
            initialValue = null
        )
//        .map { it }       //maybe I should map it to presentation-layer object, because domain-object has vars (but it's better if they will be vals)

    private val _poemViewMode = MutableLiveData( CurrentPoemFragment.PoemViewMode.initialMode )
    val poemViewMode: LiveData<CurrentPoemFragment.PoemViewMode> = _poemViewMode

    private val _pagerScrollEnabled = MutableLiveData(true)
    val pagerScrollEnabledWhenViewPoem: LiveData<Boolean> = _pagerScrollEnabled


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


    fun changePoemViewMode(toInitial: Boolean = false) {
        _poemViewMode.value =
            if (toInitial) CurrentPoemFragment.PoemViewMode.initialMode
            else           _poemViewMode.value!!.next()
    }


    fun updatePoemData(
        title: String,
        epigraph: String,
        text: String,
        author: String,
        additionalText: String,
        callback: (Boolean?, Long) -> Unit
    ) {
        //может проверку на идентичность полей чтоб лишний раз не сохранять
        //может быть может быть
        val curPoemData = currentPoem.value!!.poems.last()

        val contentAreIdentical =
            curPoemData.title == title &&
            curPoemData.epigraph == epigraph &&
            curPoemData.text == text &&
            curPoemData.additionalText == additionalText

        if (contentAreIdentical && currentPoem.value!!.author == author) {
            callback(null, 0L)
            return
        }

        val poemField = SavePoemFieldParam.packForUpdate(
            id = currentPoemId.value,
            author = author,
            title = title,
            text = text,
        )
        val poemData =
            if (contentAreIdentical) null
            else SavePoemDataParam.create(title, epigraph, text, additionalText, "")

        viewModelScope.launch {
            updatePoemUseCase(poemField, poemData).collect {
                callback(it, poemData?.timestamp ?: curPoemData.timestamp)
            }
        }
    }

    fun deletePoem(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            deletePoemUseCase(currentPoemId.value).collect {
                callback(it)
            }
        }
    }

    fun changePagerScrollEnabled(isScrollEnabled: Boolean) {
        _pagerScrollEnabled.value = isScrollEnabled
    }


}