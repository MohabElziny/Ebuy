package com.iti.android.team1.ebuy.util

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun EditText.trimText(): String = this.text.toString().trim()

fun TextInputLayout.getText(): String = this.editText?.editableText.toString().trim()

fun Fragment.showSnackBar(message: String) = Snackbar.make(requireView(), message,
    Snackbar.LENGTH_SHORT).show()