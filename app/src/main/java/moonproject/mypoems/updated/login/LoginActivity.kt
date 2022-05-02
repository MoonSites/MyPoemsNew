package moonproject.mypoems.updated.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.onClick
import moonproject.mypoems.updated.extensions.startActivity
import moonproject.mypoems.updated.extensions.toast
import moonproject.mypoems.updated.home.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {


    private val viewModel: LoginViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.checkIsPasswordExists()

        viewModel.shouldPromptNewPassword.observe(this) { shouldPromptNewPassword ->
            if (shouldPromptNewPassword) {
                showCreateNewPasswordDialog()
            }
        }

        viewModel.isAccessGranted.observe(this) {
            val color = if (it == true) 0xFFB2FF59 else 0xffB71C1C
            TextViewCompat.setCompoundDrawableTintList(passwordField, ColorStateList.valueOf(color.toInt()))

            if (it == true) {
                openHomeScreen()
            }
        }

        viewModel.userPassword.observe(this) {
            val data = CharArray(it.length) { '*' }
            passwordField.text = data.concatToString()
        }

        btnDeleteSymbol.setOnLongClickListener {
            viewModel.clearPassword()
            true
        }
        btnDeleteSymbol.onClick {
            viewModel.removeLastPasswordSym()
        }

        btnCheckPassword.onClick {
            viewModel.checkPassword()
        }

        listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach { btn ->
            btn.onClick {
                viewModel.addPasswordSym(btn.text.toString())
            }
        }

    }


    private fun showCreateNewPasswordDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.newPasswordTitle)
            .setCancelable(false)
            .setView(R.layout.dialog_login_new_password)
            .setPositiveButton(android.R.string.ok, null)
            .show()

        val field = dialog.findViewById<EditText>(R.id.passwordField)!!

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)!!.onClick {
            val text = field.text.toString()

            if (text.toIntOrNull() == null) {
                toast(R.string.errorIncorrectNewPassword)
            } else {
                viewModel.saveNewPassword(text)
                dialog.dismiss()
            }
        }

    }

    private fun openHomeScreen() {
        finish()
        startActivity<MainActivity>()
    }


}