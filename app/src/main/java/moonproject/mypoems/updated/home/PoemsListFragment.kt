package moonproject.mypoems.updated.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_poems_list.*
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.observe
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.startActivity
import moonproject.mypoems.updated.newpoem.NewPoemActivity
import moonproject.mypoems.updated.settings.SettingsActivity
import moonproject.mypoems.updated.settings.SettingsFragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PoemsListFragment : Fragment(R.layout.fragment_poems_list) {


    private val viewModel: MainViewModel by sharedViewModel()

    private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val shouldRecreate = it.data?.getBooleanExtra(SettingsFragment.SHOULD_RECREATE, false) ?: false

        if (shouldRecreate) {
            requireActivity().recreate()
        }
        viewModel.updateSearchPoemsParams()
    }


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
                R.id.menuItemSettings -> {
                    settingsLauncher.launch(
                        Intent(requireContext(), SettingsActivity::class.java)
                    )
//                    startActivity<SettingsActivity>()
                }
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
            startActivity<NewPoemActivity>()
        }

        viewModel.poemsList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

//        viewModel.currentPoemId.observe(viewLifecycleOwner) {
//            log("viewModel.currentPoemId", it)
//        }

    }

}