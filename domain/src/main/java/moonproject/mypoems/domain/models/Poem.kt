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
    var poems: List<PoemData>
}