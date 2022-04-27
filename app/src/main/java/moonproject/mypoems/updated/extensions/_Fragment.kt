package moonproject.mypoems.updated.extensions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import moonproject.mypoems.updated.extensions.intentPutUtil

inline fun <reified T: AppCompatActivity> Fragment.startActivity(vararg params: Pair<String, Any>) {
    val intent = Intent(requireContext(), T::class.java)
    intentPutUtil(intent, params)
    this.startActivity(intent)
}

//inline fun <reified T: AppCompatActivity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any>) {
//    val intent = Intent(requireContext(), T::class.java)
//    intentPutUtil(intent, params)
//    this.startActivityForResult(intent, requestCode)
//}

fun Fragment.toast(message: String, isLongToast: Boolean = false) = requireContext().toast(message, isLongToast)
fun Fragment.toast(message: Int, isLongToast: Boolean = false)    = requireContext().toast(message, isLongToast)
