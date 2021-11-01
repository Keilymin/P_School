package com.burlakov.week1application

class Constants {
    companion object {
        const val URL_REG_EX =
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.)" +
                    "{1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+" +
                    "()\\[\\]\\*$~@!:/{};']*)"
    }
}