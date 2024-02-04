package com.example.realstudy.userinterface

import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import coil.request.ImageRequest


import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.realstudy.User

@Composable
fun SettingsScreen(user: User, navController: NavController) {

    val imageUrl = user.profile.profilePicture

    Box(modifier = Modifier) {
        AsyncImage(model = imageUrl,
                   contentDescription = "background image",
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(350.dp)
                       .clipToBounds(),
                   contentScale = ContentScale.Crop
            )

        //gradient overlay at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White),
                        startY = 100f,
                        endY = 0f
                    )
                )
        )

        // Gradient overlay at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.White, Color.Transparent),
                        startY = Float.POSITIVE_INFINITY,

                        endY = 0f
                    )
                )
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
            IconButton(onClick = { navController.popBackStack() }) {

                Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier= Modifier.padding(bottom = 1.dp))
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


    }

        // Add more UI components for the study session page

        // For example, a timer, progress indicator, etc.
    }
}