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

        fun createNewField(author: String, title: String, epigraph: String, text: String, additionalText: String) = create(
            id = System.currentTimeMillis(),
            author = author,
            poemsData = listOf( SavePoemDataParam.create(title, epigraph, text, additionalText) ),
            title = title,
            text = text
        )

        fun packForUpdate(id: Long, author: String, title: String, text: String) = create(
            id = id,
            author = author,
            poemsData = listOf(),
            title = title,
            text = text
        )

        private fun create(id: Long, author: String, poemsData: List<PoemData>, title: String, text: String) = SavePoemFieldParam(
            id = id,
            author = author,
            poems = poemsData,
            currentTitle = title,
            currentFirstLine = getFirstLine(text)
        )


        private fun getFirstLine(text: String): String {
            val index = text.indexOf('\n')

            return if (index == -1 || index > 40) {
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
) : PoemData {

    companion object {

        fun create(title: String, epigraph: String, text: String, additionalText: String): SavePoemDataParam {
            return SavePoemDataParam(
                title = title,
                epigraph = epigraph,
                text = text,
                additionalText = additionalText,
                timestamp = System.currentTimeMillis()
            )
        }

    }
}



class DomainToPresenterPoemMapper {

    fun map(poemField: PoemField): AdapterPoem = AdapterPoem(
        poemField.id,
        poemField.currentTitle,
        poemField.currentFirstLine
    )

}