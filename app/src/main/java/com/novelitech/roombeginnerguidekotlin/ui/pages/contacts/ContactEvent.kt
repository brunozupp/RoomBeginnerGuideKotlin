package com.novelitech.roombeginnerguidekotlin.ui.pages.contacts

import com.novelitech.roombeginnerguidekotlin.entities.ContactEntity
import com.novelitech.roombeginnerguidekotlin.models.enums.SortType

// Event is a User Action
sealed interface ContactEvent {

    object SaveContact: ContactEvent
    data class SetFirstName(val firstName: String) : ContactEvent
    data class SetLastName(val lastName: String) : ContactEvent
    data class SetPhoneNumber(val phoneNumber: String) : ContactEvent
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent
    data class SortContacts(val sortType: SortType): ContactEvent
    data class DeleteContact(val contact: ContactEntity): ContactEvent
}