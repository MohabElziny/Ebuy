package com.iti.android.team1.ebuy.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object Decoder {

    @RequiresApi(Build.VERSION_CODES.O)
    fun encode(data: String): String {
        val result = data.toByteArray()
        for (i in result.indices) {
            result[i] = (result[i] + 1).toByte()
        }
        return Base64.getEncoder().encodeToString(result)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decode(data: String): String {
        val result = Base64.getDecoder().decode(data)
        for (i in result.indices) {
            result[i] = (result[i] - 1).toByte()
        }
        return String(result)
    }

}