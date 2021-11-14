package com.burlakov.week1application.util

import com.burlakov.week1application.models.Favorites
import com.burlakov.week1application.models.SavedPhoto
import com.burlakov.week1application.models.SearchText

class FavoritesUtil {
    companion object {
        fun setHeaders(list: MutableList<SavedPhoto>): MutableList<Favorites> {
            val newList: MutableList<Favorites> = mutableListOf()
            var text = ""
            if (list.size > 0) {
                for (sp: SavedPhoto in list) {
                    if (sp.searchText != text) {
                        newList.add(SearchText(sp.photoUserId, sp.searchText))
                        text = sp.searchText
                    }
                    newList.add(sp)
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
                    if (i is SearchText && i.searchText == text) {

                        list.remove(i)

                        return
                    }
                }
            }
        }
    }
}