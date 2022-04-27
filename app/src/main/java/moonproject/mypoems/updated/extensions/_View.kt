package moonproject.mypoems.updated.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.TextInputLayout

fun ViewGroup.inflate(@LayoutRes res: Int) = LayoutInflater.from(context).inflate(res, this, false)!!

fun View.onClick(action: () -> Unit) {
    setOnClickListener { action() }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun MaterialAlertDialogBuilder.setCornerSize(dp: Int): MaterialAlertDialogBuilder {
    (background as MaterialShapeDrawable).setCornerSize(15f.fdp)

    return this
}

fun ImageView.setVectorImage(res: Int) {
    this.setImageDrawable(
            VectorDrawableCompat.create(this.context.resources, res, this.context.theme)
    )
}

fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> RecyclerView.adapter(): T {
    return this.adapter as T
}


fun <T : View> T.withLayoutParams(width: Int, height: Int): T {
    layoutParams = ViewGroup.LayoutParams(width, height)
    return this
}


var TextInputLayout.text: String
    get() = this.editText!!.text.toString()
    set(value) { this.editText!!.setText(value) }