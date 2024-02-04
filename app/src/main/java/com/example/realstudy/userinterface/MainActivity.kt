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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
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

// For getting/displaying feed
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape

// UI design
import androidx.compose.ui.graphics.Color  // Colours
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp  // Font-size

val databaseReference =
    FirebaseDatabase.getInstance("https://studyreal-98599-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users/")

fun fetchData(u: User) {
    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.child(u.userID)
            user = User(
                u.userID,
                u.getFriends(data).toMutableList(),
                u.getProfile(data),
                u.getSessions(data)
            )
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}

val friendUser = User(
    "1232",
    mutableListOf(),
    Profile("Test", "default"),
    mutableListOf()
)
var user = User("1233", mutableListOf("1232"), Profile("John Doe", "https://firebasestorage.googleapis.com/v0/b/studyreal-98599.appspot.com/o/mog.jpg?alt=media&token=f7973466-25c4-4535-86d0-ad982938983e"), mutableListOf())



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
                        StudySessionScreen(user, navController)
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
    fetchData(user)  // Refreshing the user (for any updates while off this page)

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
    // Creating a state variable
    var allFriendSessions by remember {
        mutableStateOf<List<Triple<String, String, List<StudySession>>>>(emptyList())
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
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
                    modifier = Modifier.padding(start = 12.dp, bottom = 16.dp)
                )

                IconButton(onClick = { goToSettings() }) {

                    Icon(Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.padding(bottom = 18.dp))
                }

            }
            // Title
            // Other UI components as needed
        }
    }
    Box {
        // User's friends feed
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp)
        ) {
            fun leaderboardSort(
                sessions: List<Triple<String, String, List<StudySession>>>
            ): List<Triple<String, String, List<StudySession>>> {
                return sessions.sortedByDescending { triple ->
                    triple.third.sumOf { it.duration.toInt() }
                }
            }

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    allFriendSessions = leaderboardSort(user.getFeed(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            items(allFriendSessions.size) { index ->
                println(index)
                // Display each friend's profile as a rounded outlined box
                FriendBox(friend = allFriendSessions[index], index)
            }
        }
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

@Composable
fun FriendBox(
    friend: Triple<String, String, List<StudySession>>,
    place: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // Top bar showcasing the name
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    start = 12.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 12.dp)
        ) {
            fun isMe(name: String): String =
                if (user.profile.displayName == name) {
                    "#${place + 1} - $name (you)"
                } else {
                    "#${place + 1} - $name"
                }

            Text(
                text = isMe(friend.first),
                color =
                    if (place == 0) {
                        Color(0xFFFFD700)  // Change to the desired color when place is 0 (e.g., Color.Green)
                    } else {
                        Color.White // Change to the default color when place is not 0
                    },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        // Large box beneath for a "timeline"-esque gadget
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 36.dp,
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 18.dp)  // Distance between inner box and bottom border
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            val info = friend.third.joinToString(separator = " ───── ") {
                "${it.startTime} ➜ ${it.endTime}"
            }

            Text(
                text = "Timeline\n",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, top = 4.dp, bottom = 6.dp)
            )

            fun emptyFeed(segments: String): String =
                segments.ifEmpty {
                    "No recorded study sessions today!"
                }

            Text(
                text = emptyFeed(info),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 6.dp, top = 26.dp, bottom = 8.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
