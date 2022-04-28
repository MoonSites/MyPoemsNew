package moonproject.mypoems.updated.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.log
import moonproject.mypoems.updated.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = FragmentsPagerAdapter(this)
        pager.isUserInputEnabled = false
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                log("pager.onPageSelected", position)
                when (position) {
                    FragmentsPagerAdapter.PAGE_POEMS_LIST -> {
                        if (viewModel.isPoemOpened()) {
                            viewModel.closeCurrentPoem()
                        }
                        pager.isUserInputEnabled = false
                    }
                    else -> {
                        pager.isUserInputEnabled = true
                    }
                }
            }
        })

        viewModel.currentPoem.observe(this) {
            if (it != null) {
                pager.setCurrentItem(FragmentsPagerAdapter.PAGE_POEM_VIEW, true)
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isPoemOpened()) {
            viewModel.closeCurrentPoem()
            pager.setCurrentItem(FragmentsPagerAdapter.PAGE_POEMS_LIST, true)
        } else {
            super.onBackPressed()
        }
    }

}