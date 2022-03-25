package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.Repository
import com.sgorinov.exilehelper.core.domain.models.StaticDataGroupItem
import com.sgorinov.exilehelper.core.presentation.models.StaticGroup
import com.sgorinov.exilehelper.core.presentation.models.StaticGroupItem

class GetStaticData(private val repository: Repository) {

    suspend fun execute(): List<StaticGroup> {
        return repository.staticData().map { StaticGroup(it.id, it.label, mapEntries(it.entries)) }
    }

    private fun mapEntries(entries: List<StaticDataGroupItem>): List<StaticGroupItem> {
        return entries.map {
            val imageUrl = if (it.imageUrl != null) {
                "https://www.pathofexile.com${it.imageUrl}"
            } else {
                null
            }
            StaticGroupItem(it.id, it.label, imageUrl)
        }
    }
}