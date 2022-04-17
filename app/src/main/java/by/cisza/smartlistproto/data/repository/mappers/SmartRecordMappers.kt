package by.cisza.smartlistproto.data.repository.mappers

import by.cisza.smartlistproto.data.db.entities.DbSmartRecord
import by.cisza.smartlistproto.data.entities.SmartRecord

object SmartRecordMappers {

    private const val LIST_SEPARATOR: String = "<*>"

    fun mapDbSmartRecordToSmartRecord(from: DbSmartRecord) : SmartRecord {
        return SmartRecord(
            id = from.id,
            title = from.title,
            description = from.description,
            quantity = from.quantity,
            completedQuantity = from.completedQuantity,
            price = from.price,
            currency = from.currency,
            tags = getTags(from.tagsString)
        )
    }

    fun mapSmartRecordToDbSmartRecord(from: SmartRecord): DbSmartRecord {
        return DbSmartRecord(
            id = from.id,
            title = from.title,
            description = from.description,
            quantity = from.quantity,
            completedQuantity = from.completedQuantity,
            price = from.price,
            currency = from.currency,
            tagsString = getTagsString(from.tags)
        )
    }

    private fun getTagsString(tags: List<String>): String? {
        if (tags.isNotEmpty()) {
            val stringBuilder = StringBuilder()
            tags.forEachIndexed { _, string ->
                stringBuilder.append(string).append(LIST_SEPARATOR)
            }

            return stringBuilder.substring(0, stringBuilder.lastIndex - LIST_SEPARATOR.length)
        }

        return null
    }


    private fun getTags(tagsString: String?): List<String> {
        tagsString?.let {
            return tagsString.split(LIST_SEPARATOR)
        }
        return emptyList()
    }

}
