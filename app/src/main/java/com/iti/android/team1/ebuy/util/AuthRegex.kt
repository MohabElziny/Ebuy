package com.iti.android.team1.ebuy.util

import java.util.regex.Pattern

object AuthRegex {

    private val EMAIL_REGEX = Pattern.compile("^" +
            "[A-Za-z]" +   //Any english latter A to Z and a to z
            "(.*)" +       //it can contain many .
            "([@]{1})" +   //must have only one @
            "(.{1,})" +    //at least one or more .
            "(\\.)" +
            "(.{1,})"
    )

    private val PASWORD_REGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$")

    fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return PASWORD_REGEX.matcher(password).matches()
    }
}