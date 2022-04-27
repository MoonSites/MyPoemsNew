package moonproject.mypoems.data.models

import moonproject.mypoems.domain.models.EncodedPassword

data class PasswordData(
    val value: String
)

class PasswordDataToEncodedMapper {

    fun mapToEncoded(passwordData: PasswordData): EncodedPassword = EncodedPassword(
        passwordData.value
    )

}

class PasswordEncodedToDataMapper {

    fun mapToData(encodedPassword: EncodedPassword): PasswordData = PasswordData(
        encodedPassword.value
    )
}