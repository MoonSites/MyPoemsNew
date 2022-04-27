package moonproject.mypoems.updated.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> PoemsListFragment()
        1 -> CurrentPoemFragment()

        else -> throw IllegalStateException("FragmentsPagerAdapter.createFragment: unknown position! $position")
    }
}