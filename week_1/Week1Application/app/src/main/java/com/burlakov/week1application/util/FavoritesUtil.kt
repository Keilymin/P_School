package com.burlakov.week1application.util

import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.SearchText

class FavoritesUtil {
    companion object {
        fun setHeaders(list: MutableList<SavedPhoto>): MutableList<Favorites> {
            val newList: MutableList<Favorites> = mutableListOf()
            var text: String
            var start = 0
            var end = 0
            if (list.size > 0) {
                text = list[start].searchText
                newList.add(SearchText(list[start].photoUserId, list[start].searchText))

                while (end < list.size - 1) {
                    end++
                    if (list[end].searchText != text) {
                        for (i in start until end) {
                            newList.add(list[i])
                        }
                        start = end
                        text = list[start].searchText
                        newList.add(SearchText(list[start].photoUserId, list[start].searchText))
                    }
                }
                for (i in start..end) {
                    newList.add(list[i])
                }
            }
            return newList
        }

        fun check(list: MutableList<Favorites>, text: String) {
            var count = 0
            for (i in list) {
                if (i is SavedPhoto && i.searchText == text) {
                    count++
                }
            }
            if (count == 0) {
                for (i in list) {
                    if (i is SearchText) {
                        list.remove(i)
                        return
                    }
                }
            }
        }
    }
}