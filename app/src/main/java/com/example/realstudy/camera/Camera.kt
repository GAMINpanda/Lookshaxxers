package com.example.realstudy.camera

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor)
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

class Camera {

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
    @Composable
    fun CameraPreview(
        modifier: Modifier = Modifier,
        scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
        cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    ) {
        val coroutineScope = rememberCoroutineScope()
        val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(
            modifier = modifier,
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                // CameraX Preview UseCase
                val previewUseCase = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                coroutineScope.launch {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        // Must unbind the use-cases before rebinding them.
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, previewUseCase
                        )
                    } catch (ex: Exception) {
                        Log.e("CameraPreview", "Use case binding failed", ex)
                    }
                }

                previewView
            }
        )
    }
}