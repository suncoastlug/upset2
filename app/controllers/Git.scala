/**
 *
 */
package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Promise
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration._
import scala.concurrent.Future

import java.io.File
import org.pegdown.PegDownProcessor

import models.git.GitRepository
import org.eclipse.jgit.transport.FetchResult


object Git extends Controller {

  private val DEFAULT_GIT_URI = "git://github.com/suncoastlug/slug-pages.git"
  private val DEFAULT_GIT_DIR = "/tmp/upset/slug-pages"

  private val dir = new File( current.configuration.getString("git.dir").getOrElse(DEFAULT_GIT_DIR) )
  private val uri = current.configuration.getString("git.uri").getOrElse(DEFAULT_GIT_URI)

  private lazy val repository = GitRepository(dir)

  private lazy val markdownProcessor = new PegDownProcessor

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
      Ok ( views.html.page(markdownProcessor.markdownToHtml(c)) )
    }
  }

  def getRaw(path: String) =
    withContent(path, "master") { c =>
      Ok(c)
    }

  def fetch = Action { 
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
