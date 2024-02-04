package com.example.realstudy.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.realstudy.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.Executor

class NotificationHandler(val context: Context) {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun Permission(
        permission: String = android.Manifest.permission.CAMERA,
        rationale: String = "This permission is important for this app. Please grant the permission.",
        permissionNotAvailableContent: @Composable () -> Unit = { },
        content: @Composable () -> Unit = { }
    ) {
        val permissionState = rememberPermissionState(permission)
        PermissionRequired(
            permissionState = permissionState,
            permissionNotGrantedContent = {
                Rationale(
                    text = rationale,
                    onRequestPermission = { permissionState.launchPermissionRequest() }
                )
            },
            permissionNotAvailableContent = permissionNotAvailableContent,
            content = content
        )
    }

    @Composable
    private fun Rationale(
        text: String,
        onRequestPermission: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { /* Don't */ },
            title = {


                Text(text = "Permission request")
            },
            text = {
                Text(text)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Ok")
                }
            }
        )
    }

    fun sendNotification() { // Currently not functional
        val builder = NotificationCompat.Builder(context, "your_channel_id")
            .setContentTitle("App Notification")
            .setContentText("You've left the app.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Handle no permission later
                return
            }
            notify(1, builder.build())
        }
    }
}