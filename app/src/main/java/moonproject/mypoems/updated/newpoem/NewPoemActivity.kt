package moonproject.mypoems.updated.newpoem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_new_poem.*
import kotlinx.coroutines.launch
import moonproject.mypoems.data.storage.UserPreferences
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.usecases.poems.SaveNewPoemUseCase
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.toast
import moonproject.mypoems.updated.models.SavePoemFieldParam
import org.koin.android.ext.android.get
import java.text.SimpleDateFormat
import java.util.*

class NewPoemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_poem)

        btnCancel.onClick {
            onBackPressed()
        }

        btnSave.onClick {
            if (textField.text.isEmpty()) {
                toast(R.string.text_is_empty)
                return@onClick
            }

            savePoem {
                if (it) {
                    toast(R.string.saved)
                    finish()
                } else {
                    toast(R.string.error)
                }
            }
        }

        authorField.hint = get<UserPreferences>().defaultAuthor
        dateField.hint =
            SimpleDateFormat(PoemData.BASE_DATE_FORMAT, Locale.getDefault())
                .format(System.currentTimeMillis())

    }


    private fun savePoem(callback: (Boolean) -> Unit) {

        val poemField = SavePoemFieldParam.createNewField(
            author   = authorField     .text.toString().ifEmpty { authorField.hint.toString() },
            title    = titleField      .text.toString().ifEmpty { "***" },
            epigraph = epigraphField   .text.toString(),
            text     = textField       .text.toString(),
            additionalText = otherField.text.toString(),
            userDate       = dateField .text.toString()
        )

        lifecycleScope.launch {
            SaveNewPoemUseCase(get()).invoke(poemField).collect {
                callback(it)
            }
        }

    }

}