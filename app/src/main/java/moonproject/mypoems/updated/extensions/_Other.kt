package moonproject.mypoems.updated.extensions

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.IntRange
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

inline val Float.dp: Int
    get() = fdp.toInt()

inline val Float.fdp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

inline val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Int.withAlpha(@IntRange(from = 0, to = 255) alpha: Int) = Color.argb(alpha, red, green, blue)