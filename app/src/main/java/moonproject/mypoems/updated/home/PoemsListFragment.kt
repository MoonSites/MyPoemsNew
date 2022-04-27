package moonproject.mypoems.updated.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_poems_list.*
import kotlinx.coroutines.launch
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.domain.usecases.poems.SaveNewPoemUseCase
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.toast
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.random.Random

class PoemsListFragment : Fragment(R.layout.fragment_poems_list) {


    private val viewModel: MainViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = PoemsAdapter()

        poemsList.adapter = adapter
        poemsList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuItemSettings -> {

                }
            }
            true
        }

        createPoemFab.onClick {
            temp()
        }

        viewModel.poemsList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

    }


    private fun temp() {

        val poeField = PoemField(
            System.currentTimeMillis(),
            "Danil",
            listOf(
                PoemData(
                "sdf", "", "${Random.nextInt(1000, 100000)}", "", 0L
            )
            )
        )

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                SaveNewPoemUseCase(get()).invoke(poeField).collect {
                    toast(it)
                }
            }
        }

        toast("Done Maybe")
    }

}