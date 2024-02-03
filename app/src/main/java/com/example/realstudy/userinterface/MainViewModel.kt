package com.example.realstudy.userinterface

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val remainingTime: Int = 60

    // LiveData for user name
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    // ... (Other parts of the ViewModel)

    init {
        // Initialize the user name (you can replace it with the actual user data)
        _userName.value = "John Doe"

        // ... (Other initialization logic)
    }

    // ... (Other methods and LiveData properties)
}