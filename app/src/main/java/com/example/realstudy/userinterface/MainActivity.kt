package com.example.realstudy.userinterface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realstudy.ui.theme.RealStudyTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            RealStudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    StudySessionScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun StudySessionScreen(viewModel: MainViewModel) {
    var userName by remember { mutableStateOf("Android") }

    Column {
        Greeting(name = userName)
        StudyTimer(viewModel)
        // Other UI components for the study session screen
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

fun StudyTimer(viewModel: MainViewModel) {
    // Use the ViewModel to access the timer data and functionality
    // For simplicity, assume there's a LiveData<Int> representing remaining time
    val remainingTime = viewModel.remainingTime

    // UI components for displaying and interacting with the timer
    // For example, a countdown timer with start, pause, and reset buttons
}