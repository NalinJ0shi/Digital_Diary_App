package com.example.digitaldiary

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openPlayStoreForReview(context: Context) {
    val packageName = context.packageName
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("market://details?id=$packageName")
        setPackage("com.android.vending")
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val webIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=com.nalin.my_digitaldiary&hl=en_IN"))
        context.startActivity(webIntent)
    }
}