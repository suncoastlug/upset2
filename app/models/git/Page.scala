package models.git

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl

import org.pegdown.PegDownProcessor

import scala.xml.factory.XMLLoader
import scala.xml.{Elem, XML}
import play.api.templates.Html

trait Page {

  def title: String

  def html: Html

  def markdown: String
}

object Page {

  private val markdownProcessor = new PegDownProcessor
  private val htmlLoader        = XML.withSAXParser(new SAXFactoryImpl().newSAXParser())

  private class DefaultPage(
    val markdown: String, 
    defaultTitle: String
  ) extends Page {
    lazy val html: Html         = Html(markdownProcessor.markdownToHtml(markdown))
    private lazy val elem: Elem = htmlLoader.loadString(html.body)
    lazy val title              = (elem \\ "h1").map(_.text).headOption.getOrElse(defaultTitle)

  }

  def parse(markdown: String, defaultTitle: String = ""): Page = new DefaultPage(markdown, defaultTitle)


}


