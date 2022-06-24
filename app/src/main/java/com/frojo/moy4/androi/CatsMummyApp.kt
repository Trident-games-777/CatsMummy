package com.frojo.moy4.androi

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.frojo.moy4.androi.utils.*

private const val APPS_KEY = "HLYx3rQvLMNDxA7sXo87rR"

class CatsMummyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance().init(APPS_KEY, AppsCallback, this)
        AppsFlyerLib.getInstance().start(this)
        AppLinkData.fetchDeferredAppLinkData(this) { deepLinkCallback(convertToString(it)) }
    }
}