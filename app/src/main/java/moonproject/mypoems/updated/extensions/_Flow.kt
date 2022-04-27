package moonproject.mypoems.updated.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun <T> Flow<T>.observe(viewLifecycleOwner: LifecycleOwner, observer: (T) -> Unit): Flow<T> {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(observer)
        }
    }
    return this
}
