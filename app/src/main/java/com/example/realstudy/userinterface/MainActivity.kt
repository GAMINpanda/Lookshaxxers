package com.example.realstudy.userinterface

import android.os.Build
import com.example.realstudy.User
import com.example.realstudy.Profile

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.realstudy.R
import com.example.realstudy.StudySession
import com.example.realstudy.ui.theme.RealStudyTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

// For getting feed
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

// UI design
import androidx.compose.ui.graphics.Color  // Colours
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp  // Font-size

val databaseReference =
    FirebaseDatabase.getInstance("https://studyreal-98599-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users/")

val user =
    User("1234", mutableListOf(), Profile("John Doe", "https://firebasestorage.googleapis.com/v0/b/studyreal-98599.appspot.com/o/mog.jpg?alt=media&token=f7973466-25c4-4535-86d0-ad982938983e"), mutableListOf())


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // implement logic to check if phone already has user. if not, create default user,
        // otherwise access existing users details and create object from it

        val userExists = false

        if (userExists) {
            TODO() // if a phone has already created a user, fetch that user data and create an object from it
        } else {
            addUser(user)
        }


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            RealStudyTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomePageScreen(navController, viewModel)
                    }
                    composable("studySession") {
                        StudySessionScreen(user)
                    }
                    composable("settings") {
                        SettingsScreen(user, navController)
                    }
                }
            }
        }
    }
}


fun addUser(user: User) = database.child(user.userID).setValue(user)

@Composable
fun HomePageScreen(navController: NavHostController, viewModel: MainViewModel) {

    HomePage(
        user = user,
        startStudySession = {
            navController.navigate("studySession")
        },
        goToSettings = {
            navController.navigate("settings")
        }
    )
}

val database =
    FirebaseDatabase.getInstance("https://studyreal-98599-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users")


@Composable
fun HomePage(user: User, startStudySession: () -> Unit, goToSettings: () -> Unit) {
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


            Icon(
                painter = painterResource(id = R.drawable.baseline_people_24),
                contentDescription = null
            )


            Text(
                text = "StudyReal.",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            IconButton(onClick = { goToSettings() }) {

                Icon(Icons.Default.Settings, contentDescription = null)
            }
        }

        // Title

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
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Start Study Session",
                color = Color.White,
                style = LocalTextStyle.current.copy(fontSize = 20.sp)
            )
        }
    }
}
