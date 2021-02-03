package uz.drop.htmlparsing.data

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.FormElement
import org.jsoup.select.Elements
import kotlin.math.min

interface Repository {
    suspend fun login(login: String, password: String): Response<String>
    suspend fun parseHtml(html: String): Response<List<Data>>
}

class RepositoryImpl : Repository {
    private val url: String = "http://cabinet.hududgaz.uz/"
    private val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"

    override suspend fun login(login: String, password: String): Response<String> {
        return try {
            val loginFormResponse = Jsoup.connect(url)
//                .followRedirects(true)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute()

            // # Fill the login form
            // ## Find the form first...
            val loginForm = loginFormResponse.parse()
                .select("form#AbonentEntryForm").first() as? FormElement
            loginForm ?: return Response.Error("Login formani parse qilishda xatolik")

            // ## ... then "type" the username ...
            val loginField: Element = loginForm.select("#licshetLogin").first()
            loginField.`val`(login)


            // ## ... and "type" the password
            val passwordField: Element = loginForm.select("#passwordLogin").first()
            passwordField.`val`(password)


            // # Now send the form for login

            val loginActionResponse = loginForm.submit()
                .cookies(loginFormResponse.cookies())
                .userAgent(USER_AGENT)
                .execute()

            Response.Success(loginActionResponse.parse().html())
        } catch (e: Exception) {
            Response.Error("Xatolik sodir bo'ldi iltimos qayta urinib ko'ring", e)
        }
    }

    override suspend fun parseHtml(html: String): Response<List<Data>> {
        return try {
            val list = ArrayList<Data>()
            val document: Document = Jsoup.parse(html)
            val userDataKeys: Elements = document.select(".abonent-stat-infos .files b")
            val userDataValues: Elements = document.select(".abonent-stat-infos .files span")
            val min = min(userDataKeys.size, userDataValues.size)
            for (i in 0 until min) {
                list.add(Data(userDataKeys[i].text(), userDataValues[i].text()))
            }
            Response.Success(list)
        } catch (e: java.lang.Exception) {
            Response.Error("Parse qilishda xatolik", e)
        }

    }
}