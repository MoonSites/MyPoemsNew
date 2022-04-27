package moonproject.mypoems.updated.extensions

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import moonproject.mypoems.updated.extensions.intentPutUtil


fun Context.toast(message: String, isLongToast: Boolean = false) {
    Toast.makeText(this.applicationContext, message, if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes message: Int, isLongToast: Boolean = false) {
    toast(getString(message), isLongToast)
}

inline fun <reified T: AppCompatActivity> Context.startActivity(vararg params: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    intentPutUtil(intent, params)
    this.startActivity(intent)
}