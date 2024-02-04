package com.example.realstudy.userinterface

import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import coil.request.ImageRequest


import android.graphics.drawable.Icon
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.realstudy.User

@Composable
fun SettingsScreen(user: User, navController: NavController) {

    val imageUrl = user.profile.profilePicture


    var name by remember { mutableStateOf(user.profile.displayName) }

    // pfp top half
    Box(modifier = Modifier) {
        AsyncImage(model = imageUrl,
                   contentDescription = "background image",
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(350.dp)
                       .clipToBounds(),
                   contentScale = ContentScale.Crop
            )



    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            IconButton(onClick = { navController.popBackStack() }) { //back arrow

                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
                    .padding(bottom = 8.dp)
            )

        }

        fun updatePicture() {
            TODO()
        }

        Box(
            modifier = Modifier
                .padding(top = 250.dp),
            contentAlignment = Alignment.Center
        ) {
            // Study session button
            Button(
                onClick = { updatePicture() },
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text(text = "Update profile picture")
            }
        }



        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        fun submitName() {
            user.updateName(database, name)
            navController.popBackStack()   //back arrow
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Study session button
            Button(
                onClick = { submitName() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text(text = "Save")
            }
        }
    }
    }
}