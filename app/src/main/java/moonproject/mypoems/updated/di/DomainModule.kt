package moonproject.mypoems.updated.di

import moonproject.mypoems.domain.usecases.login.CheckPasswordUseCase
import moonproject.mypoems.domain.usecases.login.CheckPasswordUserLengthUseCase
import moonproject.mypoems.domain.usecases.login.SaveNewPasswordUseCase
import moonproject.mypoems.domain.usecases.poems.*
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
    factory { UpdatePoemUseCase(poemsRepo = get()) }
    factory { DeletePoemUseCase(poemsRepo = get()) }

    //new poem
//    factory { () }
}