package moonproject.mypoems.updated.di

import moonproject.mypoems.updated.home.MainViewModel
import moonproject.mypoems.updated.login.LoginViewModel
import moonproject.mypoems.updated.models.AdapterPoemMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        LoginViewModel(
            checkPasswordUseCase = get(),
            saveNewPasswordUseCase = get(),
            checkPasswordUserLengthUseCase = get()
        )
    }

    viewModel {
        MainViewModel(
            getSortedPoemsUseCase = get(),
            getCurrentPoemUseCase = get(),
            updatePoemUseCase = get(),
            adapterPoemMapper = AdapterPoemMapper()
        )
    }

}