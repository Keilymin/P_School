package com.burlakov.week1application.models

data class Photos(
    val photo: List<Photo>,
    val page: Int,
    val pages: Int
) {
    var searchText : String = ""
    fun hasNext() : Boolean {
        return page < pages
    }
}