/** XMLParseApp.scala
  * Created by arnold-jr on 9/20/16.
  */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.ccil.cowan.tagsoup.jaxp
import scala.xml.XML
import scala.util.Try
import net.liftweb.json
import net.liftweb.json.JsonDSL._




object XMLParseApp {

  /**
    * Parses a string looking for HTML/XML element "row" and returns content of
    * "row" attribute "Body"
    * @param line
    * @return String with body text or "invalid xml"
    */
  def rowParser(line: String): String = {
    def subParse(): String = {
      val elem = XML.loadString(line)
      val body = Map("Body" ->
        customParser.loadString((elem \ "@Body").text).text)
      val date = Map("CreationDate" -> (elem \ "@CreationDate").text)
      val viewCount = Map("ViewCount" -> (elem \ "@ViewCount").text)
      json.compactRender(body ++ date ++ viewCount)
    }
    Try(subParse()) getOrElse "invalid xml"
  }

  /**
    * Uses Tagsoup to convert (possibly mal-formed) HTML to XML
    */
  object customParser {
    val parser =
      XML.withSAXParser(new jaxp.SAXFactoryImpl().newSAXParser())

    /**
      * Loads a possibly malformed HTML string as XML element
      * @param line String of HTML text
      * @return
      */
    def loadString(line: String) = parser.loadString(line)
  }


  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: /path/to/spark-submit <input.jar> " +
        "</path/to/input.XML> </path/to/output.json>")
      System.exit(1)
    }
    val postsFileName: String = args(0)
    val outputJSONName: String = args(1)


    // Creates the new Spark configuration
    val conf = new SparkConf()
      .setAppName("Word Count Application")
      .setMaster("local[1]")

    // Creates new Spark contenxt
    val sc = new SparkContext(conf)

    // Reads in the text file as RDD and extracts body text
    val textData = sc.textFile(postsFileName, 2)
        .map(rowParser)
        .filter(_ != "invalid xml")

    (textData take 10) foreach println

    textData.saveAsTextFile(outputJSONName)

  }
}
