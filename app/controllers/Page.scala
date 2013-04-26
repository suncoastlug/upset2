package controllers

import com.petebevin.markdown.MarkdownProcessor

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS
import play.api.mvc.Action
import play.api.mvc.Controller

object Pages extends Controller {
  val mdProcessor   = new MarkdownProcessor()
  val githubBaseUrl = "https://api.github.com"
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def page(page: String) = Action {
    val key = "etag:" + page
    Async {
      buildPageRequest(page, Cache.getAs[String](key)).get().map { response =>
      	response.header("ETag") match {
      	  case None => {}
      	  case Some(etag) => Cache.set(key, etag)
      	}
      	
      	response.status match {
      	  case 200 => Ok(response.body) // TODO: cache response
      	  case 304 => Ok("cached")
      	}
      }
    }
  }
  
  private def buildPageRequest(page: String, etag: Option[String] = None) = {
    val pageUrl = githubBaseUrl + "/repos/suncoastlug/slug-pages/contents/" + page
    val request = WS.url(pageUrl).withHeaders(ACCEPT -> "application/vnd.github.v3.raw")
  
    etag match {
      case None    => request
      case Some(e) => request.withHeaders( IF_NONE_MATCH -> e)
    }
  }
}
