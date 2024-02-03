package com.example.realstudy.studytimer

fun main(args: Array<String>) {
    var flag = true
    var intput: Int // It's named intput on purpose, I'm not retarded
    var study = true
    while (flag) {
        print("Enter timer length: ")
        intput = readln().toInt()
        MainActivity(intput, study)
        print("Would you like to have another study session (0 - yes, 1 - no): ")
        flag = readln() == "0"
        study = false
        if (flag) {
            print("\nHere is your well-deserved break before your next study session:")
            MainActivity(intput / 5, study)
        } else {
            print("\nWell done, now go touch some grass you fuckin incel")
        }
    }
}