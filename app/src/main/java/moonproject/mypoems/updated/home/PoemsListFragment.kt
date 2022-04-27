package moonproject.mypoems.updated.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_poems_list.*
import kotlinx.coroutines.launch
import moonproject.mypoems.domain.usecases.poems.SaveNewPoemUseCase
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.toast
import moonproject.mypoems.updated.models.SavePoemDataParam
import moonproject.mypoems.updated.models.SavePoemFieldParam
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

        val poeField = SavePoemFieldParam(
            System.currentTimeMillis(),
            "Danil",
            RealmList(
                SavePoemDataParam("sdf", "", "${Random.nextInt(1000, 100000)}", "", 0L)
            ))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                SaveNewPoemUseCase(get()).invoke(poeField).collect {
                    toast(it)
                }
            }
        }

//        toast("Done Maybe")
    }

}




//fun flowFrom(api: CallbackBasedApi): Flow<T> = callbackFlow {
//    val callback = object : Callback { // Implementation of some callback interface
//        override fun onNextValue(value: T) {
//            // To avoid blocking you can configure channel capacity using
//            // either buffer(Channel.CONFLATED) or buffer(Channel.UNLIMITED) to avoid overfill
//            trySendBlocking(value)
//                .onFailure { throwable -> // Downstream has been cancelled or failed, can log here }
//        }
//        override fun onApiError(cause: Throwable) {
//            cancel(CancellationException("API Error", cause))
//        }
//        override fun onCompleted() = channel.close()
//    }
//    api.register(callback)
//    /*
//     * Suspends until either 'onCompleted'/'onApiError' from the callback is invoked
//     * or flow collector is cancelled (e.g. by 'take(1)' or because a collector's coroutine was cancelled).
//     * In both cases, callback will be properly unregistered.
//     */
//    awaitClose { api.unregister(callback) }
//}