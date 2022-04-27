package moonproject.mypoems.data

import moonproject.mypoems.data.models.PasswordDataToEncodedMapper
import moonproject.mypoems.data.models.PasswordEncodedToDataMapper
import moonproject.mypoems.data.storage.PasswordStorage
import moonproject.mypoems.domain.models.EncodedPassword
import moonproject.mypoems.domain.repo.PasswordRepo

class PasswordRepoImpl(
    private val passwordStorage: PasswordStorage,
    private val encodedToDataMapper: PasswordEncodedToDataMapper,
    private val dataToEncodedMapper: PasswordDataToEncodedMapper
) : PasswordRepo {

    override fun getPassword(): EncodedPassword {
        val password = passwordStorage.getPassword()
        return dataToEncodedMapper.mapToEncoded( passwordData = password )
    }

    override fun saveNewPassword(encodedPassword: EncodedPassword) {
        val password = encodedToDataMapper.mapToData(encodedPassword = encodedPassword)
        passwordStorage.saveNewPassword(password = password)
    }

    override fun getPasswordUserLength(): Int = passwordStorage.getPasswordUserLength()

    override fun saveNewPasswordUserLength(length: Int) {
        passwordStorage.saveNewPasswordUserLength(length)
    }
}