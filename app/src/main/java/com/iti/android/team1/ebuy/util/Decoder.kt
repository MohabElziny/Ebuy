package com.iti.android.team1.ebuy.util

import android.util.Base64

object Decoder {

    fun encode(data: String): String {
        val result = data.toByteArray()
        for (i in result.indices) {
            result[i] = (result[i] + 1).toByte()
        }
        return Base64.encodeToString(result, Base64.DEFAULT)
    }

    fun decode(data: String): String {
        val result = Base64.decode(data, Base64.DEFAULT)
        for (i in result.indices) {
            result[i] = (result[i] - 1).toByte()
        }
        return String(result)
    }

}