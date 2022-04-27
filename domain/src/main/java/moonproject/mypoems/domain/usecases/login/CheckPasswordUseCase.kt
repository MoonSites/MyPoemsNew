package moonproject.mypoems.domain.usecases.login

import moonproject.mypoems.domain.models.DecodedPassword
import moonproject.mypoems.domain.repo.PasswordRepo
import moonproject.mypoems.domain.utils.PasswordEncoder


class CheckPasswordUseCase(
    private val passwordRepo: PasswordRepo,
    private val passwordEncoder: PasswordEncoder
) {

    operator fun invoke(userPassword: DecodedPassword): Boolean {
        return passwordEncoder.encode(userPassword) == passwordRepo.getPassword()
    }

}