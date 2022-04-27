package moonproject.mypoems.data.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

internal open class PoemFieldRealm(
    @PrimaryKey
    override var id: Long,
    override var author: String,
    var poemsRealm: RealmList<PoemDataRealm>
) : RealmObject(), PoemField {
    constructor() : this(0L, "", RealmList())

    @Deprecated("List don't supports by realm", ReplaceWith("poemsRealm"))
    @Ignore
    override var poems: List<PoemData> = poemsRealm

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
        val poems = RealmList<PoemDataRealm>()

        poemField.poems.forEach {
            poems.add(
                mapPoemDataToRealm(it)
            )
        }

        return PoemFieldRealm(poemField.id, poemField.author, poems)
    }

    private fun mapPoemDataToRealm(poemData: PoemData): PoemDataRealm = PoemDataRealm(
        poemData.title,
        poemData.epigraph,
        poemData.text,
        poemData.additionalText,
        poemData.timestamp,
    )

}