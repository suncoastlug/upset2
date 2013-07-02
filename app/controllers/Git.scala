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

  private def withContent(path: String, branch: String)(f: String => Result) = Action {
    repository.getContent(path, branch) match {
      case None          => NotFound(path + " not found")
      case Some(content) => f(content)
    }
  }

  def initialize = 
    if (!dir.exists) 
      GitRepository.clone(uri, dir)
    else
      repository.fetch()

  def getRoot = getPage("index.html")

  def getPage(path: String) = {
    val mdPath = path.replaceAll("\\.html$", ".md")

    withContent(mdPath, "master") { c =>
      Ok ( views.html.page(markdownProcessor.markdown(c)) )
    }
  }

  def getRaw(path: String) =
    withContent(path, "master") { c =>
      Ok(c)
    }
  

}
