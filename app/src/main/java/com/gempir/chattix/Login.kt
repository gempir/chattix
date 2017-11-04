package com.gempir.chattix

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_login.*
import android.webkit.WebView
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern
import android.content.Intent




class Login : AppCompatActivity() {

    val client = OkHttpClient()
    var pattern = Pattern.compile("#access_token=(.*)&")

    val currentActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginTab = tabs.newTab()
        loginTab.text = "Login"
        tabs.addTab(loginTab)

        twitchLogin.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                val matcher = pattern.matcher(request.url.toString())

                if (matcher.find()) {
                    TwitchUserData.accessToken = matcher.group(1)
                    println(TwitchUserData.accessToken)
                    setUsername(TwitchApi.buildUsernameUrlString(TwitchUserData.accessToken))

                    val myIntent = Intent(currentActivity, Chat::class.java)
                    startActivity(myIntent)
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
        }


        twitchLogin.settings.javaScriptEnabled = true
        twitchLogin.loadUrl(TwitchApi.OAUTH_URL)
    }

    fun setUsername(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                var doc = JSONObject(response.body()?.string())
                TwitchUserData.username = doc.getJSONObject("token").getString("user_name")
            }
        })
    }
}
