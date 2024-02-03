package com.example.realstudy.userinterface

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.realstudy.ui.theme.RealStudyTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            RealStudyTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomePageScreen(navController, viewModel)
                    }
                    composable("studySession") {
                        StudySessionScreen()
                    }
                }
            }
        }
    }
}



@Composable
fun HomePageScreen(navController: NavHostController, viewModel: MainViewModel) {
    val userName by viewModel.userName.observeAsState("John Doe")

    HomePage(
        userName = userName,
        startStudySession = {
            navController.navigate("studySession")
        }
    )
}

@Composable
fun HomePage(userName: String, startStudySession: () -> Unit) {
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
        // Other UI components as needed
    }

    // Box to overlay the button at the bottom
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Study session button
        Button(
            onClick = { startStudySession() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = "Start Study Session")
        }
    }
}
