package com.github.vfedorenko.rxoauth

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Single
import io.reactivex.SingleEmitter

class OAuthWebView(context: Context, attrs: AttributeSet? = null) : WebView(context, attrs) {

    init {
        settings.apply {
            loadsImagesAutomatically = true
            blockNetworkImage = false
        }

        webViewClient = OAuthWebViewClient()
    }

    private var singleEmitter: SingleEmitter<String>? = null

    private var callbackUrl = ""
    private var tokenQuery = ""

    fun obtainToken(authUrl: String, callbackUrl: String,
                    tokenQuery: String,
                    params: Map<String, String> = mapOf(),
                    scheme: String = "https"): Single<String> {

        this.callbackUrl = callbackUrl
        this.tokenQuery = tokenQuery

        val authUriBuiler = Uri.Builder()
                .scheme(scheme)
                .encodedAuthority(authUrl)

        params.forEach {
            authUriBuiler.appendQueryParameter(it.key, it.value)
        }

        val url = authUriBuiler.build().toString()

        return Single.create {
            singleEmitter = it
            loadUrl(url)
        }
    }

    inner class OAuthWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, urlString: String): Boolean {
            val uri = Uri.parse(urlString)

            if (uri.authority == callbackUrl) {
                val token = uri.getQueryParameter(tokenQuery)

                singleEmitter?.onSuccess(token)
                return true
            }
            return false
        }
    }
}
