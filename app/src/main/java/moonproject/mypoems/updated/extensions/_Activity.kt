package moonproject.mypoems.updated.extensions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

inline fun <reified T: AppCompatActivity> FragmentActivity.startActivity(vararg params: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    intentPutUtil(intent, params)
    this.startActivity(intent)
}


fun intentPutUtil(intent: Intent, params: Array<out Pair<String, Any>>) {
    params.forEach {
        when(val sec = it.second) {
            is Int -> intent.putExtra(it.first, sec)
            is String -> intent.putExtra(it.first, sec)
            is Boolean -> intent.putExtra(it.first, sec)
            is Long -> intent.putExtra(it.first, sec)
        }
    }
}