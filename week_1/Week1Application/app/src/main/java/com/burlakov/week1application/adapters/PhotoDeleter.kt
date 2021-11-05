package com.burlakov.week1application.adapters

import kotlinx.coroutines.Job

interface PhotoDeleter {
    fun deleteFromFavorites(url : String): Job
}