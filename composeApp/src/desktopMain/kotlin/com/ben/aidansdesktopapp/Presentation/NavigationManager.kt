package com.ben.aidansdesktopapp.Presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationManager {
    var currentScreen by mutableStateOf("Home")

    fun navigateTo(screen: String) {
        currentScreen = screen
    }
}