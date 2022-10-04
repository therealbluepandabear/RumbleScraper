
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.jsoup.Jsoup
import java.io.IOException

class RumbleScraper private constructor() {
    companion object {
        private const val RUMBLE_URL = "https://rumble.com/"

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
                "${RUMBLE_URL}search/video?q=$query"
            } else {
                "${RUMBLE_URL}search/video?q=$query&page=$page"
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

                    for (element2 in element.getElementsByClass("video-item--meta video-item--views")) {
                        println("VIEWS >>> ${element2.attr("data-value")}")
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
            val doc = Jsoup.connect(RUMBLE_URL).get()

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

    fun scrapeVideoSrcFromUrl(url: String): String? {
        val document = Jsoup.connect(url).get()

        for (element in document.getElementsByTag("script")) {
            if (element.attr("type") == "application/ld+json") {
                val content = element.data()
                val array = JsonParser.parseString(content).asJsonArray

                val embedUrl = Gson().fromJson(array.get(0).asJsonObject.get("embedUrl"), String::class.java)
                var embedId = ""

                for (char in embedUrl.dropLast(1).reversed()) {
                    if (char != '/') {
                        embedId += char
                    } else {
                        break
                    }
                }

                val doc = Jsoup.connect("https://rumble.com/embedJS/u3/?request=video&ver=2&v=${embedId.reversed()}").ignoreContentType(true).get()
                val jsonData = doc.getElementsByTag("body").first()?.text()

                val mp4 = JsonParser.parseString(jsonData).asJsonObject.get("u").asJsonObject.get("mp4").asJsonObject.get("url").toString()

                return mp4.replace("\"", "")
            }
        }

        return null
    }
}

fun main() {
    println(RumbleScraper.create().scrapeVideoSrcFromUrl("https://rumble.com/v1m9oki-our-first-automatic-afk-farms-locals-minecraft-server-smp-ep3-live-stream.html"))
}