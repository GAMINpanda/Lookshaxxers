package com.example.realstudy.studytimer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start your MainActivity or any other activity here
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Optional: finish the launcher activity if you don't want it in the back stack
    }
}