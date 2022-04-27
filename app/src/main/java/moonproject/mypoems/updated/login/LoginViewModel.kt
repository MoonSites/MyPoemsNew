package moonproject.mypoems.updated.login

import androidx.annotation.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import moonproject.mypoems.domain.models.DecodedPassword
import moonproject.mypoems.domain.usecases.login.CheckPasswordUseCase
import moonproject.mypoems.domain.usecases.login.CheckPasswordUserLengthUseCase
import moonproject.mypoems.domain.usecases.login.SaveNewPasswordUseCase

class LoginViewModel(
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val saveNewPasswordUseCase: SaveNewPasswordUseCase,
    private val checkPasswordUserLengthUseCase: CheckPasswordUserLengthUseCase
) : ViewModel() {

    private val _isAccessGranted = MutableLiveData<Boolean?>()
    val isAccessGranted: LiveData<Boolean?> = _isAccessGranted

    private val _shouldPromptNewPassword = MutableLiveData(false)
    val shouldPromptNewPassword: LiveData<Boolean> = _shouldPromptNewPassword

    private val _userPassword = MutableLiveData("")
    val userPassword: LiveData<String> = _userPassword


    fun addPasswordSym(@Size(1) symbol: String) {
        _userPassword.value = _userPassword.value + symbol

        if ( checkPasswordUserLengthUseCase(_userPassword.value!!.length) ) {
            checkPassword()
        }
    }

    fun removeLastPasswordSym() {
        val value = _userPassword.value!!
        if (value.isNotEmpty()) {
            _userPassword.value = value.substring(0, value.lastIndex)
        }
    }

    fun checkPassword() {
        val userPass = DecodedPassword(_userPassword.value!!)
        _isAccessGranted.value = checkPasswordUseCase(userPassword = userPass)
    }

    fun checkIsPasswordExists() {
        if ( checkPasswordUseCase(userPassword = DecodedPassword.EmptyPassword) ) {
            _shouldPromptNewPassword.value = true
        }
    }

    fun saveNewPassword(newPasswordValue: String) {
        val userPass = DecodedPassword(newPasswordValue)
        saveNewPasswordUseCase(decodedPassword = userPass, newPasswordValue.length)
        _isAccessGranted.value = true
    }

}