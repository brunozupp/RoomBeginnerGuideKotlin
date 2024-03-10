package com.novelitech.roombeginnerguidekotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.novelitech.roombeginnerguidekotlin.database.ContactDatabase
import com.novelitech.roombeginnerguidekotlin.ui.pages.contacts.ContactPage
import com.novelitech.roombeginnerguidekotlin.ui.pages.contacts.ContactViewModel
import com.novelitech.roombeginnerguidekotlin.ui.theme.RoomBeginnerGuideKotlinTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "roombeginnerguide.db"
        ).build()
    }

    // If the viewModel needs parameters I need to use Factory
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.contactDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomBeginnerGuideKotlinTheme {

                val state by viewModel.state.collectAsState()

                ContactPage(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}