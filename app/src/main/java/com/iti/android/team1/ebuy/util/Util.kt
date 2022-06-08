package com.iti.android.team1.ebuy.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.trimText(): String {
    return this.text.toString().trim()
}

fun TextInputLayout.getText(): String = this.editText?.editableText.toString().trim()