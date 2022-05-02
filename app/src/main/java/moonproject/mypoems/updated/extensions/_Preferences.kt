package moonproject.mypoems.updated.extensions

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

fun PreferenceFragmentCompat.getPref(key: String): Preference = findPreference(key)!!
fun Preference.onClick(action: (pref: Preference) -> Unit) {
    setOnPreferenceClickListener {
        action(it)
        true
    }
}