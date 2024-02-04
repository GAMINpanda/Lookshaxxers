package com.example.realstudy.studytimer

import java.time.LocalTime

class StudySegments() {
    private val segments: MutableList<Pair<LocalTime,LocalTime>> = mutableListOf()
    fun add(pair: Pair<LocalTime,LocalTime>){
        segments.add(pair)
    }
    fun get(): List<Pair<LocalTime,LocalTime>> = segments
}