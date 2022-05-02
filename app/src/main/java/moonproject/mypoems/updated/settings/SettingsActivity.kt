package moonproject.mypoems.updated.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_settings.*
import moonproject.mypoems.updated.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {


    private val viewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, SettingsFragment())
        }

        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(SettingsFragment.SHOULD_RECREATE, viewModel.shouldRecreate)
        )

    }

}