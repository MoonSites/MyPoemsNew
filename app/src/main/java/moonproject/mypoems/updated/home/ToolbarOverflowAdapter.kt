package moonproject.mypoems.updated.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import moonproject.mypoems.updated.R

class ToolbarOverflowAdapter(
    context: Context,
    private val resource: Int,
    objects: List<OverflowItem>
) : ArrayAdapter<ToolbarOverflowAdapter.OverflowItem>(context, resource, objects) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        val data = getItem(position) ?: return view

        val titleField = view.findViewById<TextView>(R.id.title)
        val subtitleField = view.findViewById<TextView>(R.id.subtitle)

        titleField.setText(data.title)
        subtitleField.text = data.subtitle

        subtitleField.isVisible = data.subtitle.isNotEmpty()

        return view
    }


    data class OverflowItem(
        val id: Int,
        @StringRes val title: Int,
        val subtitle: String,
    )
}