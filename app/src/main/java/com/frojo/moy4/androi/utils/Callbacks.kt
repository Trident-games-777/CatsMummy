package com.frojo.moy4.androi.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.facebook.applinks.AppLinkData
import com.frojo.moy4.androi.models.DataStructure

private var appsData = false
private var deepData = false
private var data: DataStructure = DataStructure()

val appsDataCallback: (MutableMap<String, Any>?) -> Unit = {
    data = data.copy(appData = it)
    appsData = true
    if (deepData) liveData.postValue(data)
}

val deepLinkCallback: (String) -> Unit = {
    data = data.copy(deepData = it)
    deepData = true
    if (appsData) liveData.postValue(data)
}

private val liveData: MutableLiveData<DataStructure> = MutableLiveData()
val callbackLiveData: LiveData<DataStructure> get() = liveData

fun convertToString(appLinkData: AppLinkData?): String {
    return appLinkData?.targetUri.toString()
}

object AppsCallback : AppsFlyerConversionListener {
    override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
        appsDataCallback(data)
    }

    override fun onConversionDataFail(p0: String?) {
    }

    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
    }

    override fun onAttributionFailure(p0: String?) {
    }
}