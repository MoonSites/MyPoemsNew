package moonproject.mypoems.updated.di

import moonproject.mypoems.domain.usecases.login.CheckPasswordUseCase
import moonproject.mypoems.domain.usecases.login.CheckPasswordUserLengthUseCase
import moonproject.mypoems.domain.usecases.login.SaveNewPasswordUseCase
import moonproject.mypoems.domain.usecases.poems.GetCurrentPoemUseCase
import moonproject.mypoems.domain.usecases.poems.GetSortedPoemsUseCase
import moonproject.mypoems.domain.usecases.poems.SearchPoemsParamsUseCase
import moonproject.mypoems.domain.usecases.poems.UpdatePoemUseCase
import moonproject.mypoems.domain.utils.PasswordEncoder
import org.koin.dsl.module

val domainModule = module {

    //login
    factory { CheckPasswordUseCase  (passwordRepo = get(), passwordEncoder = PasswordEncoder()) }
    factory { SaveNewPasswordUseCase(passwordRepo = get(), passwordEncoder = PasswordEncoder()) }
    factory { CheckPasswordUserLengthUseCase(passwordRepo = get()) }

    //main
    factory { GetSortedPoemsUseCase(poemsRepo = get()) }
    factory { SearchPoemsParamsUseCase(poemsRepo = get()) }
    factory { GetCurrentPoemUseCase(poemsRepo = get()) }
    factory { UpdatePoemUseCase() }

    //new poem
//    factory { () }
}