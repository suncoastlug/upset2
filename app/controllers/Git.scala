/**
 *
 */
package controllers

import java.io.File

import models.git.GitRepository
import models.Page

import org.eclipse.jgit.transport.FetchResult

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Promise
import play.api.libs.concurrent.Execution.Implicits._
import play.api.templates.Html

import scala.concurrent.duration._
import scala.concurrent.Future
 
object Git extends Controller {

  private val DEFAULT_GIT_URI = "git://github.com/suncoastlug/slug-pages.git"
  private val DEFAULT_GIT_DIR = "/tmp/upset/slug-pages"

  private val dir = new File( current.configuration.getString("git.dir").getOrElse(DEFAULT_GIT_DIR) )
  private val uri = current.configuration.getString("git.uri").getOrElse(DEFAULT_GIT_URI)

  private lazy val repository = GitRepository(dir)

  def initialize = 
    if (!dir.exists) 
      GitRepository.clone(uri, dir)
    else
      repository.fetch()

  def menu: Html = Html {
    repository.getContent("menu.html", "master").getOrElse("[no menu]")
  }

  def root = page("index.html")

  def page(path: String) = {
    val mdPath = path.replaceAll("\\.html$", ".md")

    Action { implicit request =>
      repository.getContent(mdPath, "master") match {
        case None           => NotFound(path + " not found")
        case Some(markdown) => Ok {
          views.html.git.page( Page.parse(markdown, path) )
        }
      }
    }
  }

  def fetch = Action { implicit request =>
    val resultFuture  = scala.concurrent.Future { Right(repository.fetch) }
    val timeoutFuture = Promise.timeout(Left("git fetch timed out"), 2.seconds)
    Async {
      Future.firstCompletedOf(Seq(resultFuture, timeoutFuture)).map {
        case Right(result) => Ok(views.html.git.fetch(result))
        case Left(error)   => InternalServerError(error)
      }
    }
  }
  

}
