package moonproject.mypoems.data.storage

import android.content.Context

class UserPreferences(context: Context) {

    val prefs = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE)


    val useDarkTheme: Boolean?
        get() {
            return when (prefs.getString(KEY_THEME, "")) {
                "true" -> true
                "false" -> false
                else -> null
            }
        }

    val useDynamicColors: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_COLORS, true)

    val defaultAuthor: String
        get() = prefs.getString(KEY_DEF_AUTHOR, "") ?: ""



    companion object {
        const val APP_PREFS_NAME = "app_prefs"
        private const val KEY_THEME = "theme"
        private const val KEY_DYNAMIC_COLORS = "useDynamicColors"
        private const val KEY_DEF_AUTHOR = "defAuthor"
    }

}