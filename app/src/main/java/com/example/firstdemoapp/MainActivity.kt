package com.example.firstdemoapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.firstdemoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        with(binding.content.webview) {

            settings.javaScriptEnabled = true
            settings.allowContentAccess = true
            addJavascriptInterface(MyJavaScriptInterface(this@MainActivity), "Android")

            val page =
                "https://www.flipkart.com/philips-hp8100-60-hair-dryer/p/itmff6qnhzbshgwt?pid=HDRFF2VTK5ZWYV63&lid=LSTHDRFF2VTK5ZWYV63IDZNFY&marketplace=FLIPKART&sattr[]=color&st=color&otracker=hp_omu_Best%2525252Bof%2525252BElectronics_5_3.dealCard.OMU_VXQGYLXW75FQ_3"

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    val script =
                        "(function() { var button = document.getElementsByClassName('css-1dbjc4n r-1awozwy r-14lw9ot r-y47klf r-1s7ton2 r-eu3ka r-1777fci r-1aockid')[0]; \n" +
                                "button.addEventListener('click', function(event) {\n" +
                                "Android.onButtonClick(\"Wishlist Icon\");\n" +
                                "});})();"
                    view?.evaluateJavascript(script, null)
                }
            }
            loadUrl(page)
        }
    }

    class MyJavaScriptInterface internal constructor(context: Context) {
        private val context: Context

        init {
            this.context = context
        }

        // This method can be called from JavaScript
        @JavascriptInterface
        public fun onButtonClick(event: String) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Wishlist Icon Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}