package com.example.realstudy.userinterface

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.realstudy.R
import com.example.realstudy.StudySession
import com.example.realstudy.User
import com.example.realstudy.formatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudySessionScreen(user: User) {

    val images = mutableListOf<String>()

    var workTime by remember { mutableStateOf("25") }
    var breakTime by remember { mutableStateOf("5") }

    var timerState by remember { mutableStateOf(TimerState.Stopped) }
    var currentTime by remember { mutableIntStateOf(0) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }

    DisposableEffect(Unit) {
        onDispose {
            // Reset variables when the composable is disposed (e.g., when navigating away)
            timerState = TimerState.Stopped
            currentTime = 0
            startTime = LocalTime.now()
        }
    }

    // Use LaunchedEffect to restart the timer when currentTime reaches 0
    LaunchedEffect(currentTime) {
        if (currentTime == 0 && timerState != TimerState.Stopped) {
            timerState = TimerState.Stopped
            startTime = LocalTime.now()
        }
    }

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
            Text(
                text = "StudyReal",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Work time input
        OutlinedTextField(
            value = workTime,
            onValueChange = { workTime = it },
            label = { Text("Work Time (minutes)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Break time input
        OutlinedTextField(
            value = breakTime,
            onValueChange = { breakTime = it },
            label = { Text("Break Time (minutes)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Start button
        Button(
            onClick = {
                val studyTime = workTime.toInt()
                val breakTimeValue = breakTime.toInt()

                timerState = TimerState.Running
                startTime = LocalTime.now()

                CoroutineScope(Dispatchers.Default).launch {
                    // Start the timer coroutine for work time
                    while (currentTime < studyTime * 60 && timerState == TimerState.Running) {
                        currentTime = LocalTime.now().toSecondOfDay() - startTime.toSecondOfDay()
                        delay(1000)
                    }

                    if (timerState == TimerState.Running) {
                        val format = formatter(startTime, LocalTime.now())
                        // Work time completed, now start break time
                        val session = StudySession(
                            format.first,
                            format.second,
                            maxOf(0, (studyTime + breakTimeValue) * 60 - currentTime),  // Ensure non-negative value
                            images
                        )
                        user.addSession(database, session)
                        withContext(Dispatchers.Main) {
                            timerState = TimerState.Break
                            startTime = LocalTime.now()

                            CoroutineScope(Dispatchers.Default).launch {
                                // Start the timer coroutine for break time
                                while (currentTime <= (studyTime + breakTimeValue) * 60 && timerState == TimerState.Break) {
                                    currentTime = LocalTime.now().toSecondOfDay() - startTime.toSecondOfDay()
                                    delay(1000)
                                }

                                if (timerState == TimerState.Break) {

                                    // Break time completed, update database or perform any other necessary actions
                                    withContext(Dispatchers.Main) {
                                        timerState = TimerState.Stopped

                                    }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (timerState == TimerState.Stopped) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Start")
                } else {
                    Icon(painter = painterResource(id = R.drawable.baseline_pause_24), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pause")
                }

            }
        }

        // Timer display
        Text(
            text = when (timerState) {
                TimerState.Stopped -> "Timer Not Started"
                TimerState.Running -> "Time Remaining: ${(workTime.toInt() * 60 - currentTime) / 60} min ${(workTime.toInt() * 60 - currentTime) % 60} sec"
                TimerState.Break -> "Break Time Remaining: ${(breakTime.toInt() * 60 - currentTime) / 60} min ${(breakTime.toInt() * 60 - currentTime) % 60} sec"
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium
        )

        // Add more UI components for the study session page
        // For example, a timer, progress indicator, etc.
    }

}

enum class TimerState {
    Stopped,
    Running,
    Break
}
