package moonproject.mypoems.updated.extensions

import moonproject.mypoems.updated.BuildConfig

fun log(title: String, msg: Any?) {
    if (BuildConfig.DEBUG)
        android.util.Log.i(title, parseMsg(msg))
}

fun logE(title: String, msg: Any?, throwable: Throwable? = null) {
    if (BuildConfig.DEBUG)
        android.util.Log.e(title, parseMsg(msg), throwable)
}

infix fun <T: Any?> T.log(title: String): T {
    if (BuildConfig.DEBUG)
        android.util.Log.i(title, parseMsg(this))
    return this
}

private fun parseMsg(msg: Any?): String =
    when (msg) {
        is Collection<*> -> msg.joinToString(separator = ",", prefix = "[", postfix = "]")
        is Array<*>      -> msg.joinToString(separator = ",", prefix = "[", postfix = "]")
        else             -> msg.toString()
    }