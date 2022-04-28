package moonproject.mypoems.updated.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_poems_list.*
import kotlinx.coroutines.launch
import moonproject.mypoems.domain.usecases.poems.SaveNewPoemUseCase
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.log
import moonproject.mypoems.updated.extensions.observe
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.toast
import moonproject.mypoems.updated.models.SavePoemFieldParam
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.random.Random

class PoemsListFragment : Fragment(R.layout.fragment_poems_list) {


    private val viewModel: MainViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = PoemsAdapter { id -> viewModel.openCurrentPoem(id) }
        val toolbarSearchView = (toolbar.menu.findItem(R.id.menuItemSearch).actionView as SearchView)

        poemsList.adapter = adapter
        poemsList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            createPoemFab.visibility = if (isOpen) View.INVISIBLE else View.VISIBLE
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuItemSearchType -> {
                    viewModel.toggleFilterField()
                }
                R.id.menuItemSorting -> {
                    viewModel.toggleSorting()
                }
                R.id.menuItemSettings -> {}
            }
            true
        }

        toolbarSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setFilterText(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false
        })

        createPoemFab.onClick {
            temp()
        }

        viewModel.poemsList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        viewModel.currentPoemId.observe(viewLifecycleOwner) {
            log("viewModel.currentPoemId", it)
        }
        viewModel.currentPoem.observe(viewLifecycleOwner) {
            log("viewModel.currentPoem", it)
        }

    }


    private fun temp() {
        val a: List<Char> = ('a'..'z') + ('A'..'Z')
        val b = CharArray(10) { a[Random.nextInt(0, a.size)] }
        val poeField = SavePoemFieldParam.create(
            author = "Danil",
            title = b.joinToString(""),
            epigraph = "",
            text = "${Random.nextInt(1000, 100000)}",
            additionalText = "",
        )

        lifecycleScope.launch {
            SaveNewPoemUseCase(get()).invoke(poeField).collect {
                toast(it)
            }
        }

    }

}