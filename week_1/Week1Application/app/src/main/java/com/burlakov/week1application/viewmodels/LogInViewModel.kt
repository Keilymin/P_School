package com.burlakov.week1application.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.repositories.UserRepository
import kotlinx.coroutines.launch

class LogInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val logInResult = MutableLiveData<Boolean>()


    fun singIn(username: String) {
        viewModelScope.launch {
            logInResult.value = userRepository.logIn(username)
        }
    }
}