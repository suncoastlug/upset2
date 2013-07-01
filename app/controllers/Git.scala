/**
 *
 */
package controllers

import play.api._
import play.api.mvc._
import com.petebevin.markdown.MarkdownProcessor
import play.api.Play.current
import java.io.File

import models.git.GitRepository

object Git extends Controller {

  private val DEFAULT_GIT_URI = "git://github.com/suncoastlug/slug-pages.git"
  private val DEFAULT_GIT_DIR = "/tmp/upset/slug-pages"

  private val dir = new File( current.configuration.getString("git.dir").getOrElse(DEFAULT_GIT_DIR) )
  private val uri = current.configuration.getString("git.uri").getOrElse(DEFAULT_GIT_URI)

  private lazy val repository = GitRepository(dir)

  private lazy val markdownProcessor = new MarkdownProcessor()

  def initialize = if (!dir.exists) GitRepository.clone(uri, dir)

  def getIndexPage = TODO

  def getPage(path: String) = Action { 
    repository.getContent(path, "master") match {
      case None => NotFound("unable to find page " + path)
      case Some(body) => Ok( views.html.page(markdownProcessor.markdown(body)) )
    }
  }

  def getImage(path: String) = TODO
  

}
