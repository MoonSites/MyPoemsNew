package moonproject.mypoems.updated.home

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_poem.*
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.inflate
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.models.AdapterPoem

class PoemsAdapter(private val onItemCLicked: (id: Long) -> Unit) : RecyclerView.Adapter<PoemsAdapter.VH>() {


    private val data = mutableListOf<AdapterPoem>()


    inner class VH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(obj: AdapterPoem) {
            poemTitle.text = obj.title
            poemFirstString.text = obj.firstLine

            containerView.onClick {
                onItemCLicked(obj.id)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent.inflate(R.layout.item_list_poem))
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])
    override fun getItemCount(): Int = data.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<AdapterPoem>) {
        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

}