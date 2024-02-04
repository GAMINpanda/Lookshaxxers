package com.example.realstudy.userinterface

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realstudy.Profile
import com.example.realstudy.User

class MainViewModel : ViewModel() {
    val remainingTime: Int = 60

    // LiveData for user name
    private val user =
        User("1234", mutableListOf(), Profile("John Doe", "defaultpicture.jpg"), mutableListOf())
    val userData: User get() = user

    val userName: String = user.profile.displayName

    // ... (Other parts of the ViewModel)

    init {
        // Initialize the different properties of the user
        // ... (Other initialization logic)
    }

    // ... (Other methods and LiveData properties)
}