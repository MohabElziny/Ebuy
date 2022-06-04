package com.iti.android.team1.ebuy.util

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi

object Decoder {

    @RequiresApi(Build.VERSION_CODES.O)
    fun encode(data: String): String {
        val result = data.toByteArray()
        for (i in result.indices) {
            result[i] = (result[i] + 1).toByte()
        }
        return Base64.encodeToString(result, Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decode(data: String): String {
        val result = Base64.decode(data, Base64.DEFAULT)
        for (i in result.indices) {
            result[i] = (result[i] - 1).toByte()
        }
        return String(result)
    }

}