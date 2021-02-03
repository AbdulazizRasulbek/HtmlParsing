package uz.drop.htmlparsing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.FormElement
import java.net.CookieHandler
import java.net.CookieManager


class MainActivity3 : AppCompatActivity() {
    private val url: String = "http://cabinet.hududgaz.uz/"
    val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"
    val USERNAME = "03004185619"
    val PASSWORD = "977699339"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        CoroutineScope(Dispatchers.Default).launch {
//            CookieHandler.setDefault(CookieManager())
            val loginFormResponse = Jsoup.connect(url)
//                .followRedirects(true)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute()

            // # Fill the login form
            // ## Find the form first...
            val loginForm = loginFormResponse.parse()
                .select("form#AbonentEntryForm").first() as FormElement
            checkElement("Login Form", loginForm)


            // ## ... then "type" the username ...
            val loginField: Element = loginForm.select("#licshetLogin").first()
            checkElement("Login Field", loginField)
            loginField.`val`(USERNAME)


            // ## ... and "type" the password
            val passwordField: Element = loginForm.select("#passwordLogin").first()
            checkElement("Password Field", passwordField)
            passwordField.`val`(PASSWORD)


            // # Now send the form for login
            val loginActionResponse = loginForm.submit()
                .cookies(loginFormResponse.cookies())
                .userAgent(USER_AGENT)
                .execute()

            println(loginActionResponse.parse().html())
        }
    }

    fun checkElement(name: String, elem: Element?) {
        if (elem == null) {
            throw RuntimeException("Unable to find $name")
        }
    }

}