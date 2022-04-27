package moonproject.mypoems.domain.usecases.login

import moonproject.mypoems.domain.repo.PasswordRepo


class CheckPasswordUserLengthUseCase(
    private val passwordRepo: PasswordRepo,
) {

    operator fun invoke(userPasswordLength: Int): Boolean {
        return passwordRepo.getPasswordUserLength() == userPasswordLength
    }

}