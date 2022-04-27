package moonproject.mypoems.data.storage

import android.content.Context
import androidx.core.content.edit
import moonproject.mypoems.data.models.PasswordData

class SharedPrefPasswordStorage(context: Context) : PasswordStorage {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getPassword(): PasswordData = PasswordData(prefs.getString(KEY_PASSWORD, "") ?: "")
    override fun getPasswordUserLength(): Int = prefs.getInt(KEY_PASSWORD_LENGTH, 0)

    override fun saveNewPassword(password: PasswordData) {
        prefs.edit { putString(KEY_PASSWORD, password.value) }
    }

    override fun saveNewPasswordUserLength(length: Int) {
        prefs.edit { putInt(KEY_PASSWORD_LENGTH, length) }
    }


    companion object {
        private const val PREFS_NAME = "password_prefs"
        private const val KEY_PASSWORD = "KEY_PASSWORD"
        private const val KEY_PASSWORD_LENGTH = "KEY_PASSWORD_LENGTH"
    }
}