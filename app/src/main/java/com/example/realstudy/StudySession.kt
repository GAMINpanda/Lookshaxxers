package com.example.realstudy

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Time
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StudySession(
    var startTime: String,
    var endTime: String,
    val duration: Int,
    val images: List<String>)

@RequiresApi(Build.VERSION_CODES.O)
fun formatter(start: LocalTime, end: LocalTime): Pair<String, String> {
    val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    return Pair(start.format(dtf), end.format(dtf))
}