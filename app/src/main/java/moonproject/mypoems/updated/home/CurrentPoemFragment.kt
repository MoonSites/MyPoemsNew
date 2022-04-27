package moonproject.mypoems.updated.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_current_poem.*
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.onClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrentPoemFragment : Fragment(R.layout.fragment_current_poem) {


    private val viewModel: MainViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnChangeViewMode.onClick {

        }

        btnCopy.onClick {

        }

        btnDelete.onClick {

        }
    }

}