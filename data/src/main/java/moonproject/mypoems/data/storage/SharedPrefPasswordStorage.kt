package moonproject.mypoems.data.storage

import androidx.core.content.edit
import moonproject.mypoems.data.models.PasswordData

class SharedPrefPasswordStorage(
    private val userPreferences: UserPreferences
) : PasswordStorage {

//    private val prefs = context.getSharedPreferences(UserPreferences.APP_PREFS_NAME, Context.MODE_PRIVATE)

    override fun getPassword(): PasswordData = PasswordData(userPreferences.prefs.getString(KEY_PASSWORD, "") ?: "")
    override fun getPasswordUserLength(): Int = userPreferences.prefs.getString(KEY_PASSWORD_LENGTH, "10")?.toIntOrNull() ?: 10

    override fun saveNewPassword(password: PasswordData) {
        userPreferences.prefs.edit { putString(KEY_PASSWORD, password.value) }
    }

    override fun saveNewPasswordUserLength(length: Int) {
        userPreferences.prefs.edit { putString(KEY_PASSWORD_LENGTH, length.toString()) }
    }


    companion object {
//        private const val PREFS_NAME = "password_prefs"
        private const val KEY_PASSWORD = "KEY_PASSWORD"
        private const val KEY_PASSWORD_LENGTH = "KEY_PASSWORD_LENGTH"
    }
}