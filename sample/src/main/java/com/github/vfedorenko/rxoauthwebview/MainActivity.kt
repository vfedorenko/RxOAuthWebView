package com.github.vfedorenko.rxoauthwebview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val AUTH_URL = "github.com/login/oauth/authorize"
        const val CLIENT_ID = "e1358db690c64012ea7f"
        const val SCOPE = "user repo"
        const val CALLBACK_URL = "vfedorenko.by"
        const val TOKEN_QUERY = "code"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onGetTokenClick(v: View) {
        oauth_webview.visibility = View.VISIBLE

        val params = hashMapOf(
                "client_id" to CLIENT_ID,
                "scope" to SCOPE)

        oauth_webview.obtainToken(AUTH_URL, CALLBACK_URL, TOKEN_QUERY, params)
                .subscribe(
                        {
                            oauth_webview.visibility = View.GONE
                            token.text = it
                        },
                        {
                            oauth_webview.visibility = View.GONE
                            token.text = it.message
                        })
    }
}
