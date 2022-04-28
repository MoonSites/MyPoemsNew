package moonproject.mypoems.updated.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        PAGE_POEMS_LIST -> PoemsListFragment()
        PAGE_POEM_VIEW  -> CurrentPoemFragment()

        else -> throw IllegalStateException("FragmentsPagerAdapter.createFragment: unknown position! $position")
    }


    companion object {
        const val PAGE_POEMS_LIST = 0
        const val PAGE_POEM_VIEW = 1
    }
}