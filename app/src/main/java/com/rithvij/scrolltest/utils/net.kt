package com.rithvij.scrolltest.utils

import java.net.HttpURLConnection
import java.net.URL

fun remoteUrlExists(URLName: String) : Boolean {
// https://stackoverflow.com/a/4596483/8608146
    return try {
        HttpURLConnection.setFollowRedirects(false)
        // note : you may also need
        //        HttpURLConnection.setInstanceFollowRedirects(false)
        val con = URL(URLName).openConnection() as HttpURLConnection
        con.requestMethod = "HEAD"
        println(con.responseCode)
        (con.responseCode == HttpURLConnection.HTTP_OK)
    }
    catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
