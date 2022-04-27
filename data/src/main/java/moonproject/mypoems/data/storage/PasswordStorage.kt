package moonproject.mypoems.data.storage

import moonproject.mypoems.data.models.PasswordData

interface PasswordStorage {

    fun getPassword(): PasswordData
    fun saveNewPassword(password: PasswordData)

    fun getPasswordUserLength(): Int
    fun saveNewPasswordUserLength(length: Int)
}