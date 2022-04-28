package moonproject.mypoems.domain.models

interface PoemData {
    var title: String
    var epigraph: String
    var text: String
    var additionalText: String
    var timestamp: Long
}

interface PoemField {
    var id: Long
    var author: String
    val poems: List<PoemData>

    var currentTitle: String
    var currentFirstLine: String
}

data class GetPoemsParams(
    val sorting: Sorting,
    val filterField: FilterField = FilterField.Title,
    val filterText: String = ""
) {
    enum class Sorting {
        ASCENDING, DESCENDING;

        companion object {
            val defaultValue = ASCENDING.name
        }
    }

    enum class FilterField(val fieldName: String) {
        Title(PoemField::currentTitle.name),
        FirstLine(PoemField::currentFirstLine.name);

        companion object {
            val defaultValue = Title.name
        }
    }
}