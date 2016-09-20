/** XMLParseApp.scala
  * Created by arnold-jr on 9/20/16.
  */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import org.ccil.cowan.tagsoup.jaxp
import scala.xml._
import scala.util.Try
import net.liftweb.json




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
      val body = customParser.loadString(elem \ "@Body" text)
      body text
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
    if (args.length != 1) {
      System.err.println("Requires HDFS input or local path name as argument")
      System.exit(1)
    }
    val postsFileName: String = args(0)

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

  }
}
