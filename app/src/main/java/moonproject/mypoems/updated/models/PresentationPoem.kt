package moonproject.mypoems.updated.models

import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

data class AdapterPoem(
    val id: Long,
    val title: String,
    val firstLine: String,
)

data class SavePoemFieldParam(
    override var id: Long,
    override var author: String,
    override var poems: List<PoemData>,
    override var currentTitle: String,
    override var currentFirstLine: String
) : PoemField {

    companion object {

        fun create(author: String, title: String, epigraph: String, text: String, additionalText: String) = SavePoemFieldParam(
            System.currentTimeMillis(),
            author,
            listOf( createPoemData(title, epigraph, text, additionalText) ),
            title,
            getFirstLine(text)
        )


        private fun createPoemData(title: String, epigraph: String, text: String, additionalText: String): SavePoemDataParam {
            return SavePoemDataParam(
                title = title,
                epigraph = epigraph,
                text = text,
                additionalText = additionalText,
                timestamp = System.currentTimeMillis()
            )
        }

        private fun getFirstLine(text: String): String {
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
}

data class SavePoemDataParam(
    override var title: String,
    override var epigraph: String,
    override var text: String,
    override var additionalText: String,
    override var timestamp: Long
) : PoemData



class DomainToPresenterPoemMapper {

    fun map(poemField: PoemField): AdapterPoem = AdapterPoem(
        poemField.id,
        poemField.currentTitle,
        poemField.currentFirstLine
    )

}