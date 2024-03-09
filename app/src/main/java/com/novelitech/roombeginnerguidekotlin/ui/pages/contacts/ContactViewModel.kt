package com.novelitech.roombeginnerguidekotlin.ui.pages.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novelitech.roombeginnerguidekotlin.daos.contact.ContactDao
import com.novelitech.roombeginnerguidekotlin.entities.ContactEntity
import com.novelitech.roombeginnerguidekotlin.models.enums.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao: ContactDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    // flatMapLatest -> It takes a flow (_sortType in this case) and each time it changes
    // we transform this new value in a new Flow, in this case the contacts returned from the
    // DAO class that returns a Flow
    private val _contacts = _sortType.flatMapLatest { sortType ->
        when(sortType) {
            SortType.FIRST_NAME -> dao.getOrderedByFirstName()
            SortType.LAST_NAME -> dao.getOrderedByLastName()
            SortType.PHONE_NUMBER -> dao.getOrderedByPhoneNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ContactState())

    // Combining these 3 Flows in just one Flow. So, when at least one of them changes it will
    // be fired
    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContactState())

    fun onEvent(event: ContactEvent) {
        when(event) {
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.delete(event.contact)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update { it.copy(isAddingContact = false) }
            }
            ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
                    return
                }

                val contact = ContactEntity(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                )

                viewModelScope.launch {
                    dao.upsert(contact)
                }

                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = "",
                    )
                }
            }
            is ContactEvent.SetFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }
            is ContactEvent.SetLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value = event.sortType
            }
        }
    }
}