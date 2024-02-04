package com.example.realstudy.studytimer

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

class StudyTimer(studyTime: Int, breakTime: Int){
    var sCounter = studyTime * 60
    var bCounter = breakTime * 60
    @RequiresApi(Build.VERSION_CODES.O)
    var startTime = LocalTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    fun sTime(): Pair<LocalTime,LocalTime>{
        startTime = LocalTime.now()
        //Disable apps function

        while (sCounter >= 1){
            sCounter -= 1
            Thread.sleep(1000)
            print("e")
        }
        //Enable apps
        return Pair(startTime,LocalTime.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bTime(){
        startTime = LocalTime.now()
        while(bCounter >= 1){
            bCounter -= 1
            Thread.sleep(1000)
            print("b")
        }
    }
}