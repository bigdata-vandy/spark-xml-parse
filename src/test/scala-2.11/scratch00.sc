import org.ccil.cowan.tagsoup.jaxp
import net.liftweb.json
import net.liftweb.json.JsonDSL._
import scala.xml.XML

object customParser {
  val parser =
    XML.withSAXParser(new jaxp.SAXFactoryImpl()
      .newSAXParser())
  def loadString(line: String) = parser.loadString(line)
}

val line = "<p> some text </p> <p>Obviously, randomly generating code would be impractical, so how could I do this?</p>"
val elem = customParser.loadString(line)
val body = elem text

val foo = Map("a" -> ((elem \ "p").text), "b" -> ((elem \ "p").text))

json.compactRender(foo)

