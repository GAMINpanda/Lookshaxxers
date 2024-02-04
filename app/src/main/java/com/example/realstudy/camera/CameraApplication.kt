package com.example.realstudy.camera

import android.media.ImageReader
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.view.Surface
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.realstudy.R
import java.io.File
import java.util.*

private var imageReader: ImageReader? = null
private val IMAGE_WIDTH = 640
private val IMAGE_HEIGHT = 480
private val IMAGE_FORMAT = ImageFormat.JPEG

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CameraActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        openCamera()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val cameraId = getCameraId()
            try {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            takePicture()
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            cameraDevice = null
        }
    }

    private fun getCameraId(): String {
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                return cameraId
            }
        }
        throw RuntimeException("No back-facing camera found.")
    }

    private fun takePicture() {
        if (cameraDevice == null) return

        val captureBuilder =
            cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                ?.apply {
                    addTarget(/* Your image capture surface */)
                }

        cameraDevice?.createCaptureSession(
            listOf(/* Your image capture surface */),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    captureBuilder?.build()?.let {
                        session.capture(it, captureCallback, backgroundHandler)
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    // Handle configuration failure
                }
            }, backgroundHandler
        )
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            // Image capture completed, you can handle the result here
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if (cameraDevice == null) {
            openCamera()
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    private fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        backgroundThread?.join()
        backgroundThread = null
        backgroundHandler = null
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
    }
}