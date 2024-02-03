package com.example.realstudy.userinterface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.realstudy.ui.theme.RealStudyTheme
import com.example.realstudy.userinterface.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            RealStudyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    HomePageScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun HomePageScreen(viewModel: MainViewModel) {
    val userName by viewModel.userName.observeAsState("John Doe")

    HomePage(
        userName = userName,
//        startStudySession = {
//            // Handle starting the study session here
//            // You can navigate to another screen or perform other actions
//        }
    )
}

@Composable
fun HomePage(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.Menu, contentDescription = null)
            Icon(Icons.Default.Star, contentDescription = null)
        }

        // Greeting message
        Text(
            text = "Welcome back, $userName!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Study session button
//        Button(
//            onClick = { startStudySession() },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//        ) {
//            Text(text = "Start Study Session")
//        }

        // Other UI components as needed
    }
}
