import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

fun main(args: Array<String>) {
    try {
        val query = readln()
        val doc = Jsoup.connect("https://rumble.com/search/video?q=$query").get()
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