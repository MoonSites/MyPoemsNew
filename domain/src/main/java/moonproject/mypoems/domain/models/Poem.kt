package moonproject.mypoems.domain.models

data class PoemData(
    val title: String,
    val epigraph: String,
    val text: String,
    val additionalText: String,
    val timestamp: Long,
)

data class PoemField(
    val id: Long,
    val author: String,
    val poems: List<PoemData>,
)