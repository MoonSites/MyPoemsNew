package moonproject.mypoems.domain.usecases.login

import moonproject.mypoems.domain.models.DecodedPassword
import moonproject.mypoems.domain.repo.PasswordRepo
import moonproject.mypoems.domain.utils.PasswordEncoder

class SaveNewPasswordUseCase(
    private val passwordRepo: PasswordRepo,
    private val passwordEncoder: PasswordEncoder
) {

    operator fun invoke(decodedPassword: DecodedPassword, passwordUserLength: Int) {
        passwordRepo.saveNewPassword(encodedPassword = passwordEncoder.encode(decodedPassword))
        passwordRepo.saveNewPasswordUserLength(passwordUserLength)
    }

}