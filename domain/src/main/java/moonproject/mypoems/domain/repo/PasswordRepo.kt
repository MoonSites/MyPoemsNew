package moonproject.mypoems.domain.repo

import moonproject.mypoems.domain.models.EncodedPassword

interface PasswordRepo {

    fun getPassword(): EncodedPassword
    fun saveNewPassword(encodedPassword: EncodedPassword)

    fun getPasswordUserLength(): Int
    fun saveNewPasswordUserLength(length: Int)
}