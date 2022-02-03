package com.originalstocks.pdfmeregerdemo

import java.text.SimpleDateFormat
import java.util.*

fun getTimestamp(): String {
    return SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH).format(Calendar.getInstance().time)
}