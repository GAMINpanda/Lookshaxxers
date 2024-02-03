package com.example.realstudy.studytimer

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
class StudyTimer(time: Int){
    var counter = time
    @RequiresApi(Build.VERSION_CODES.O)
    val startTime = LocalTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    fun time(): Pair<LocalTime,LocalTime>{
        while (counter >= 0){
            counter -= 1
            Thread.sleep(60000)
        }

        return Pair(startTime,LocalTime.now())
    }
}