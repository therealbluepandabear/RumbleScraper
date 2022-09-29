import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

fun getRumbleSearchResultsForQuery(query: String, page: Int = 1) {
    try {
        val url = if (page <= 1) {
            "https://rumble.com/search/video?q=$query"
        } else {
            "https://rumble.com/search/video?q=$query&page=$page"
        }

        val doc = Jsoup.connect(url).get()

        val titles: MutableList<Element> = mutableListOf()

        for (element in doc.getElementsByClass("video-listing-entry")) {
            for (element2 in element.getElementsByClass("video-item--title")) {
                titles.add(element2)
            }
        }

        for (element in titles) {
            println(element.text())
        }
    } catch (exception: IOException) {
        exception.printStackTrace()
    }
}

fun main() {
    var currentPage = 1
    val query = readln()
    getRumbleSearchResultsForQuery(query)

    while (true) {
        if (readln() == "next page") {
            currentPage++
            getRumbleSearchResultsForQuery(query, currentPage)
        }
    }
}