import org.jsoup.Jsoup
import java.io.IOException

fun getRumbleSearchResultsForQuery(query: String, page: Int = 1) {
    try {
        val url = if (page <= 1) {
            "https://rumble.com/search/video?q=$query"
        } else {
            "https://rumble.com/search/video?q=$query&page=$page"
        }

        val doc = Jsoup.connect(url).get()

        if (doc.getElementsByClass("video-listing-entry").size == 0) {
            println("No results found")
        } else {
            for (element in doc.getElementsByClass("video-listing-entry")) {
                for (element2 in element.getElementsByClass("video-item--title")) {
                    println("${element2.text()}'\n${"-".repeat(100)}")
                }
            }
        }

        println("Page $page")
    } catch (exception: IOException) {
        exception.printStackTrace()
    }
}

fun getRumbleEditorPicks() {
    try {
        val doc = Jsoup.connect("https://rumble.com/").get()

        for (element in doc.getElementsByClass("tabs tab-editor-picks")) {
            for (element2 in element.getElementsByClass("mediaList-list container content top-earners without-show-more-link")) {
                for (element3a in element2.getElementsByClass("mediaList-item")) {
                    for (element4a in element3a.getElementsByClass("mediaList-heading size-medium")) {
                        println(element4a.text())
                    }
                }
            }
        }
    } catch (exception: IOException) {
        exception.printStackTrace()
    }
}

fun getInput() {
    val query = readln()
    var currentPage = 1

    while (true) {
        when(query.lowercase()) {
            "editor picks" -> {
                getRumbleEditorPicks()
                getInput()
            }

            "next page" -> {
                currentPage++
                getRumbleSearchResultsForQuery(query, currentPage)
                getInput()
            }

            "help" -> {
                println("'next page'        >>> Shows videos on the next page if it exists")
                println("'editor picks      >>> Shows current editor picks")
                getInput()
            }

            else -> {
                getRumbleSearchResultsForQuery(query)
                getInput()
            }
        }
    }
}
fun main() {
    println("Enter search query below")
    getInput()
}