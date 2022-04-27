package moonproject.mypoems.updated.models

import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

data class AdapterPoem(
    val id: Long,
    val title: String,
    val firstLine: String,
)

class AdapterPoemMapper {

    fun map(poemField: PoemField): AdapterPoem {
        val lastChangedVersion = poemField.poems.last()

        return AdapterPoem(
            poemField.id,
            lastChangedVersion.title,
            getFirstLine(lastChangedVersion)
        )
    }


    private fun getFirstLine(lastPoemData: PoemData): String {
        val text = lastPoemData.text
        val index = text.indexOf('\n')

        return if (index == -1) {
            val lastIndex = minOf(text.length, 40)
            val shouldAddThreeDots = if (text.length > 40) "..." else ""
            text.substring(0, lastIndex) + shouldAddThreeDots
        } else {
            text.replaceRange(index, text.length, "")
        }
    }

}