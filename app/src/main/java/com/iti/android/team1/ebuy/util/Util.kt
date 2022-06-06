package com.iti.android.team1.ebuy.util

import android.widget.EditText

fun EditText.trimText(): String {
    return this.text.toString().trim()
}