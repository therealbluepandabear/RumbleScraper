import org.jsoup.Jsoup
import java.io.IOException

class RumbleScraper private constructor() {
    companion object {
        fun create(): RumbleScraper {
            return RumbleScraper()
        }
    }

    private fun drawSeparator() {
        println("-".repeat(100))
    }

    fun scrapeByQuery(query: String, page: Int = 1) {
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
                        println(element2.text())
                    }

                    for (element2 in element.getElementsByClass("video-item--by")) {
                        for (element3 in element2.getElementsByClass("ellipsis-1")) {
                            if (element3.getElementsByClass("video-item--by-verified verification-badge-icon").isNotEmpty()) {
                                println("CREATOR >>> ${element3.text()} (V)")
                            } else {
                                println("CREATOR >>> ${element3.text()}")
                            }
                        }
                    }

                    drawSeparator()
                }

                println("Page $page")
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    fun scrapeEditorPicks() {
        try {
            val doc = Jsoup.connect("https://rumble.com/").get()

            for (element in doc.getElementsByClass("tabs tab-editor-picks")) {
                for (element2 in element.getElementsByClass("mediaList-list container content top-earners without-show-more-link")) {
                    for (element3 in element2.getElementsByClass("mediaList-item")) {
                        for (element4 in element3.getElementsByClass("mediaList-heading size-medium")) {
                            println(element4.text())
                            drawSeparator()
                        }
                    }
                }
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}


fun getInput() {
    val query = readln()
    var currentPage = 1

    val scraper = RumbleScraper.create()

    while (true) {
        if (query != "next page") {
            currentPage = 1
        }

        when(query.lowercase()) {
            "editor picks" -> {
                scraper.scrapeEditorPicks()
            }

            "next page" -> {
                currentPage++
                scraper.scrapeByQuery(query, currentPage)
            }

            "help" -> {
                println("'next page'        >>> Shows videos on the next page if it exists")
                println("'editor picks      >>> Shows current editor picks")
            }

            else -> {
                scraper.scrapeByQuery(query)
            }
        }
        getInput()
    }
}
fun main() {
    println("Enter search query below")
    getInput()
}