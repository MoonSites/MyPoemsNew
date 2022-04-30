package moonproject.mypoems.updated.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_current_poem.*
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField
import moonproject.mypoems.updated.R
import moonproject.mypoems.updated.extensions.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class CurrentPoemFragment : Fragment(R.layout.fragment_current_poem) {


    private val viewModel: MainViewModel by sharedViewModel()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    private var dateSpinnerAdapter: ArrayAdapter<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            poemButtonsParent .isVisible = !isOpen
            poemButtonsDivider.isVisible = !isOpen
        }

        btnChangeViewMode.onClick {
            viewModel.changePoemViewMode()
        }

        btnSave.onClick {
            viewModel.updatePoemData(
                title.text.toString(),
                epigraph.text.toString(),
                text.text.toString(),
                autor.text.toString(),
                dop.text.toString(),
            ) { isSuccess, newDate ->
                if (isSuccess) {
                    toast(R.string.saved)

//                    val a = dateSpinnerAdapter ?: return@updatePoemData
//                    a.add(dateFormat.format(newDate))
//                    spinner.setSelection(a.count - 1)

                } else {
                    toast(R.string.saving_error)
                }
            }
        }

        btnDelete.onClick {
            showDeletePoemDialog()
        }

        btnCopy.onClick {
            copyPoemTextToClipboard()
        }

        viewModel.poemViewMode.observe(viewLifecycleOwner) { editPoemMode ->

            when (editPoemMode!!) {
                PoemViewMode.VIEW -> {
                    btnCopy.visible()
                    btnSave.gone()
                    btnDelete.gone()
                    setEditFieldsEnabled(false)
                    setEditFieldsVisibility(false)
                }
                PoemViewMode.EDIT -> {
                    btnCopy.gone()
                    btnSave.visible()
                    btnDelete.visible()
                    setEditFieldsEnabled(true)
                    setEditFieldsVisibility(true)
                }
            }
            btnChangeViewMode.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), editPoemMode.modeIcon)
            )
        }


        viewModel.currentPoem.observe(viewLifecycleOwner) { poemField ->
            log("CurrentPoem viewModel.currentPoem", poemField)
            initScreen()
            poemField ?: return@observe
//            val currentPoemData = poemField?.poems?.lastOrNull() ?: return@observe //list should be not empty     //so show error or smth else

            autor.setText(poemField.author)
            initSpinner(poemField)

        }
    }

    private fun copyPoemTextToClipboard() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("", text.text.toString())
        clipboard.setPrimaryClip(clipData)
        toast(R.string.copied)
    }


    private fun initScreen() {
        viewModel.changePoemViewMode(toInitial = true)
        setEditFieldsEnabled(false)
    }

    private fun showData(poemData: PoemData) {
        title   .setText(poemData.title)
        epigraph.setText(poemData.epigraph)
        text    .setText(poemData.text)
        dop     .setText(poemData.additionalText)

        setEditFieldsVisibility(viewModel.poemViewMode.value == PoemViewMode.EDIT)
    }

    private fun setEditFieldsVisibility(isEnabled: Boolean) {
        epigraph.isVisible = epigraph.text.isNotEmpty() || isEnabled

        val isAdditionalVisible = dop.text.isNotEmpty() || isEnabled
        dop                  .isVisible = isAdditionalVisible
        additionalTextDivider.isVisible = isAdditionalVisible
    }

    private fun initSpinner(poemField: PoemField) {
        val poemsData = poemField.poems.map { dateFormat.format(it.timestamp) }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, R.id.spinnerItemTv, poemsData)

        dateSpinnerAdapter = adapter
        initSpinnerBackground()
        spinner.adapter = adapter
        spinner.setSelection(poemsData.lastIndex)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                log("spinner onItemSelected pos", position)
                showData(poemField.poems[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun initSpinnerBackground() {
        val drawable = GradientDrawable()
//            drawable.setColor(Color.parseColor(prefs.spinnerBackColor))
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            val overlayColor = 0x70000000

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.colorFilter = BlendModeColorFilter(overlayColor, BlendMode.DARKEN)
            } else {
                @Suppress("DEPRECATION")
                drawable.setColorFilter(overlayColor, PorterDuff.Mode.DARKEN)
            }
        }

        drawable.setColor(0xff42A5F5.toInt())
        drawable.cornerRadius = 10f
        drawable.alpha = 240
        spinner.setPopupBackgroundDrawable(drawable)
    }

    private fun setEditFieldsEnabled(isEnabled: Boolean) {
        listOf(title, epigraph, text, autor, dop).forEach {
            it.isCursorVisible = isEnabled
            it.isEnabled       = isEnabled
        }
    }


    private fun showDeletePoemDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm)
            .setMessage(R.string.delete_poem_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePoem { isSuccess ->
                    val res = if (isSuccess) R.string.deleted else R.string.error
                    toast(res)

                    viewModel.closeCurrentPoem()
                }
            }
            .setNeutralButton(android.R.string.cancel, null)
            .show()
    }



    enum class PoemViewMode(@DrawableRes val modeIcon: Int) {
        VIEW(R.drawable.ic_edit),
        EDIT(R.drawable.ic_view),
        ;

        fun next(): PoemViewMode = when (this) {
            VIEW -> EDIT
            EDIT -> VIEW
        }


        companion object {
            val initialMode = VIEW
        }
    }

}