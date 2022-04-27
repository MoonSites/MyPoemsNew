package moonproject.mypoems.domain.utils

import moonproject.mypoems.domain.models.DecodedPassword
import moonproject.mypoems.domain.models.EncodedPassword

class PasswordEncoder {

    fun encode(password: DecodedPassword): EncodedPassword {
        val encodedValue = password.value
        return EncodedPassword(encodedValue)
    }

//    fun decode(password: EncodedPassword): DecodedPassword {
//        val decodedValue = password.value
//        return DecodedPassword(decodedValue)
//    }

}