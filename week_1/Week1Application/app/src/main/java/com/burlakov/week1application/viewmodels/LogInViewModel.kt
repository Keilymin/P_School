package com.burlakov.week1application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burlakov.week1application.repositories.UserRepository
import kotlinx.coroutines.launch

class LogInViewModel(private val userRepository: UserRepository) : ViewModel() {
    val logInResult: LiveData<Boolean>
        get() = _logInResult

    private val _logInResult = MutableLiveData<Boolean>()
    

    fun singIn(username: String) = viewModelScope.launch {
        _logInResult.value = userRepository.logIn(username)
    }
}