package com.omii.noorulhuda

import android.app.Activity
import android.webkit.JavascriptInterface
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import org.json.JSONArray
import java.util.Calendar
import android.Manifest
import android.content.pm.PackageManager
import android.webkit.GeolocationPermissions
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import android.graphics.Color

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private var geoCallback: GeolocationPermissions.Callback? = null
    private var geoOrigin: String? = null

    @Suppress("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(7, 25, 20)
        window.navigationBarColor = Color.rgb(7, 25, 20)
        webView = WebView(this)
        webView.setBackgroundColor(Color.rgb(7, 25, 20))
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setGeolocationEnabled(true)
        webView.settings.loadsImagesAutomatically = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    callback?.invoke(origin, true, false)
                } else {
                    geoOrigin = origin
                    geoCallback = callback
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        401
                    )
                }
            }
        }
        webView.addJavascriptInterface(PrayerBridge(), "PrayerBridge")
        setContentView(webView)
        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class PrayerBridge {
        @JavascriptInterface fun schedulePrayerNotifications(json: String) {
            getSharedPreferences("prayer_alerts", MODE_PRIVATE).edit().putString("times", json).putBoolean("enabled", true).apply()
            runOnUiThread {
                if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 402)
                } else PrayerAlarmReceiver.scheduleAll(this@MainActivity, json)
            }
        }
        @JavascriptInterface fun cancelPrayerNotifications() {
            getSharedPreferences("prayer_alerts", MODE_PRIVATE).edit().putBoolean("enabled", false).apply()
            PrayerAlarmReceiver.cancelAll(this@MainActivity)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 402) {
            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                val prefs = getSharedPreferences("prayer_alerts", MODE_PRIVATE)
                if (prefs.getBoolean("enabled", false)) PrayerAlarmReceiver.scheduleAll(this, prefs.getString("times", "[]") ?: "[]")
            }
        }
        if (requestCode == 401) {
            val granted = grantResults.any { it == PackageManager.PERMISSION_GRANTED }
            geoCallback?.invoke(geoOrigin, granted, false)
            geoCallback = null
            geoOrigin = null
        }
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }
}
