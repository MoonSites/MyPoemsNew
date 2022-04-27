package moonproject.mypoems.data.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import moonproject.mypoems.domain.models.PoemData
import moonproject.mypoems.domain.models.PoemField

open class PoemFieldRealm(
    @PrimaryKey
    var id: Long,
    var author: String,
    var poems: RealmList<PoemDataRealm>,
) : RealmObject() {
    constructor() : this(0L, "", RealmList())
}

open class PoemDataRealm(
    var title: String,
    var epigraph: String,
    var text: String,
    var additionalText: String,
    var timestamp: Long,
) : RealmObject() {
    constructor() : this("", "", "", "", 0L)
}

class PoemsMapper() {

    fun mapFieldToRealm(obj: PoemField): PoemFieldRealm = PoemFieldRealm(
        obj.id,
        obj.author,
        mapPoemDataToRealm(obj.poems),
    )

    fun mapFieldToDomain(obj: PoemFieldRealm): PoemField = PoemField(
        obj.id,
        obj.author,
        mapRealmToPoemData(obj.poems),
    )

    fun mapDataToRealm(poemData: PoemData): PoemDataRealm = PoemDataRealm(
        title = poemData.title,
        epigraph = poemData.epigraph,
        text = poemData.text,
        additionalText = poemData.additionalText,
        timestamp = poemData.timestamp
    )


    private fun mapPoemDataToRealm(poems: List<PoemData>): RealmList<PoemDataRealm> {
        val out = RealmList<PoemDataRealm>()

        poems.forEach {
            val mappedObj = mapDataToRealm(it)
            out.add(mappedObj)
        }

        return out
    }

    private fun mapRealmToPoemData(poems: RealmList<PoemDataRealm>): List<PoemData> {
        return poems.map { PoemData(
            title = it.title,
            epigraph = it.epigraph,
            text = it.text,
            additionalText = it.additionalText,
            timestamp = it.timestamp
        ) }
    }

}
