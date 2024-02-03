package com.example.realstudy.studytimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.app.Application
import com.google.firebase.FirebaseApp

class MainActivity(time: Int = 30, study: Boolean = true) : AppCompatActivity() {
    var counter = (time * 60 * 1000).toLong()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        object : CountDownTimer(counter, 1000) {
            //if (study) {
                //Add function to disable apps
            //}

            override fun onTick(millisUntilFinished: Long) {
                println((millisUntilFinished % 60000).toString() + "m " +
                        (millisUntilFinished / 1000).toString() + "s")
            }
            override fun onFinish() {
                //Add function to re-allow apps
                println("Done")
            }
        }.start()
    }
}