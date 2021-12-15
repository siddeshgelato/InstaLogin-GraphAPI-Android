package com.crazy.instalogin_graphapi.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.common.Configuration
import com.crazy.instalogin_graphapi.common.Configuration.appId
import com.crazy.instalogin_graphapi.common.Configuration.appSecret
import com.crazy.instalogin_graphapi.common.Configuration.redirectURI
import com.crazy.instalogin_graphapi.dialog.InstagramLoginDialog
import com.crazy.instalogin_graphapi.network.ApiClient
import com.crazy.instalogin_graphapi.network.NetworkService
import com.crazy.instalogin_graphapi.common.Constant
import com.crazy.instalogin_graphapi.common.InternetUtils
import com.crazy.instalogin_graphapi.common.PreferenceHelper
import com.crazy.instalogin_graphapi.common.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_insta.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */
const val KEY_RESULT_INSTAGRAM = "KEY_RESULT_INSTAGRAM"
const val REQUEST_CODE_INSTAGRAM = 1002
const val KEY_REQUEST_CODE = "request_code"

const val KEY_APP_ID = "KEY_APP_ID"
const val KEY_APP_SECRET = "KEY_APP_SECRET"
const val KEY_INSTA_REDIRECT_URL = "KEY_INSTA_REDIRECT_URL"

class InstagramLauncherActivity : AppCompatActivity() {

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta)

        getExtra()

        initResultLauncher()

        buttonLoginInstagram.setOnClickListener {
            if (PreferenceHelper.defaultPrefs(this@InstagramLauncherActivity)
                    .getString(PreferenceHelper.ACCESS_TOKEN, "").isNullOrEmpty()
            ) {
                loginInstagram()
            } else {
                resultLauncher?.launch(
                    Intent(
                        this@InstagramLauncherActivity,
                        InstagramGalleryActivity::class.java
                    )
                )
            }
        }
    }

    private fun getExtra() {
        appId = intent.getStringExtra(KEY_APP_ID) ?: ""
        appSecret = intent.getStringExtra(KEY_APP_SECRET) ?: ""
        redirectURI = intent.getStringExtra(KEY_INSTA_REDIRECT_URL) ?: ""
    }

    private fun loginInstagram() {

        if (InternetUtils.isInternetConnected(this)) {
            val mAuthUrl = (Constant.AUTH_URL
                    + "?client_id=${appId}"
                    + "&redirect_uri=$redirectURI"
                    + "&response_type=code&display=touch&scope=user_profile")

            val dialog = InstagramLoginDialog.newInstance(
                this,
                mAuthUrl,
                redirectURI,
                object : InstagramLoginDialog.OAuthDialogListener {
                    override fun onComplete(accessToken: String?) {
                        val accessToken1 = accessToken!!.replace("#_", "")
                        getAccessToken(accessToken1)
                    }

                    override fun onError(error: String?) {
                        Toast.makeText(this@InstagramLauncherActivity, error!!, Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            dialog.show()

            /*      val builder = CustomTabsIntent.Builder()
                  val customTabsIntent = builder.build()
                  customTabsIntent.intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                  customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  customTabsIntent.launchUrl(
                      this, Uri.parse(mAuthUrl)
                  )*/

        } else {
            showToast(getString(R.string.msg_no_internet_connection))
        }
    }

    /* override fun onResume() {
         super.onResume()
         val code = intent?.data?.getQueryParameter("code")
         if (code != null) {
             val accessToken1 = code!!.replace("#_", "")
             getAccessToken(accessToken1)
         } else {
             Toast.makeText(this@InstagramLauncherActivity, "error!!", Toast.LENGTH_SHORT).show()
         }
     }
 */

    private fun getAccessToken(authCode: String) {
        val call =
            ApiClient.getClient(Constant.ACCESS_TOKEN_BASE, this)
                ?.create(NetworkService::class.java)
        call?.getAccessToken(
            code = authCode,
            client_id = appId,
            client_secret = appSecret,
            redirect_uri = redirectURI
        )!!.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable) {

                Log.e("TAG", "UserOnFailure ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                try {
                    val json = JSONObject(response.body()!!)
                    val accessToken = json.getString("access_token")
                    val userId = json.getString("user_id")
                    PreferenceHelper.defaultPrefs(this@InstagramLauncherActivity)
                        .set(PreferenceHelper.ACCESS_TOKEN, accessToken)
                    PreferenceHelper.defaultPrefs(this@InstagramLauncherActivity)
                        .set(PreferenceHelper.USER_ID, userId)

                    resultLauncher?.launch(
                        Intent(
                            this@InstagramLauncherActivity,
                            InstagramGalleryActivity::class.java
                        )
                    )
                } catch (e: Exception) {
                    showToast(e.localizedMessage)
                    e.printStackTrace()
                }

            }
        })

    }

    fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.putExtra(KEY_REQUEST_CODE, REQUEST_CODE_INSTAGRAM)
                    setResult(Activity.RESULT_OK, result.data)
                    finish()
                }
            }

    }


    private fun showToast(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}
