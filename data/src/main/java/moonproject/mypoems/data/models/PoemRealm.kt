package moonproject.mypoems.data.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

internal open class PoemFieldRealm(
    @PrimaryKey
    override var id: Long,
    override var author: String,
    var poemsRealm: RealmList<PoemDataRealm>,

    override var currentTitle: String,
    override var currentFirstLine: String
) : RealmObject(), PoemField {
    constructor() : this(0L, "", RealmList(), "", "")

    @Deprecated("List don't supports by realm", ReplaceWith("poemsRealm"))
    override val poems: List<PoemData> get() = poemsRealm

}

internal open class PoemDataRealm(
    override var title: String,
    override var epigraph: String,
    override var text: String,
    override var additionalText: String,
    override var timestamp: Long,
) : RealmObject(), PoemData {
    constructor() : this("", "", "", "", 0L)
}


class PoemsToRealmMapper {

    internal fun mapToRealm(poemField: PoemField): PoemFieldRealm {
        val poemsRealm = RealmList<PoemDataRealm>()

        poemField.poems.forEach {
            poemsRealm.add(
                mapPoemDataToRealm(it)
            )
        }

        return PoemFieldRealm(
            id = poemField.id,
            author = poemField.author,
            poemsRealm = poemsRealm,
            currentTitle = poemField.currentTitle,
            currentFirstLine = poemField.currentFirstLine
        )
    }

    internal fun mapPoemDataToRealm(poemData: PoemData): PoemDataRealm = PoemDataRealm(
        poemData.title,
        poemData.epigraph,
        poemData.text,
        poemData.additionalText,
        poemData.timestamp,
    )

}