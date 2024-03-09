package com.novelitech.roombeginnerguidekotlin.ui.pages.contacts

import com.novelitech.roombeginnerguidekotlin.entities.ContactEntity
import com.novelitech.roombeginnerguidekotlin.models.enums.SortType

data class ContactState(
    val contacts: List<ContactEntity> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false, // Determine if the dialog of adding contact is visible
    val sortType: SortType = SortType.FIRST_NAME
)
