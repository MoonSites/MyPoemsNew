package moonproject.mypoems.updated.settings

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import moonproject.mypoems.data.storage.UserPreferences
import moonproject.mypoems.domain.models.GetPoemsParams
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.getPref
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : PreferenceFragmentCompat() {


    private val viewModel: SettingsViewModel by sharedViewModel()


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = UserPreferences.APP_PREFS_NAME

        setPreferencesFromResource(R.xml.settings_screen, rootKey)

        val passPref = getPref("KEY_PASSWORD") as EditTextPreference
        val passLengthPref = getPref("KEY_PASSWORD_LENGTH") as EditTextPreference
        val defaultAuthor = getPref("defAuthor") as EditTextPreference

        val searchType = getPref("KEY_FILTER_FIELD") as ListPreference
        val sortingType = getPref("KEY_SORTING") as ListPreference

        ////////////

        passPref.setOnBindEditTextListener {
            it.filters = arrayOf(InputFilter.LengthFilter(9))
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.maxLines = 1
            it.setSelectAllOnFocus(true)
        }
        passLengthPref.setOnBindEditTextListener {
            it.filters = arrayOf(InputFilter.LengthFilter(1))
            it.maxLines = 1
            it.setSelectAllOnFocus(true)
            it.hint = "9"
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

        passLengthPref.setOnPreferenceChangeListener { _, rawNewValue ->
            val newValue = rawNewValue.toString()
            newValue.length >= passPref.text!!.length
        }

        //////////////

        defaultAuthor.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            it.maxLines = 1
            it.setSelectAllOnFocus(true)
//            it.setHint(R.string.pref_default_author_value)
        }

        //////////////

        searchType.entries     = arrayOf(
            getString(R.string.searchByTitle),
            getString(R.string.searchByFirstLine),
        )
        searchType.entryValues = arrayOf(
            GetPoemsParams.FilterField.Title.name,
            GetPoemsParams.FilterField.FirstLine.name,
        )


        sortingType.entries     = arrayOf(
            getString(R.string.sortNewFirst),
            getString(R.string.sortOldFirst),
        )
        sortingType.entryValues = arrayOf(
            GetPoemsParams.Sorting.DESCENDING.name,
            GetPoemsParams.Sorting.ASCENDING.name,
        )


        getPref("theme").setOnPreferenceChangeListener { _, newValue ->
            val mode = when (newValue.toString()) {
                "true"  -> AppCompatDelegate.MODE_NIGHT_YES
                "false" -> AppCompatDelegate.MODE_NIGHT_NO
                else    -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            true
        }

        getPref("useDynamicColors").setOnPreferenceChangeListener { _, _ ->
            viewModel.shouldRecreate = true
            requireActivity().recreate()
            true
        }

    }


    companion object {
        const val SHOULD_RECREATE = "SHOULD_RECREATE"
//        const val SHOULD_UPDATE_LIST = "SHOULD_UPDATE_LIST"   //лист .. Ференц .
    }


}