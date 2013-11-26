package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Promise
import play.api.libs.concurrent.Execution.Implicits._
import play.api.templates.Html
import scala.concurrent.duration._
import scala.concurrent.Future
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.cache.Cached

import com.github.theon.uri.dsl._
import models.meetup._
import com.github.nscala_time.time.Imports._

object Meetup extends Controller {
  private val KEY        = current.configuration.getString("meetup.key")
  private val GROUP_NAME = current.configuration.getString("meetup.group").getOrElse("Suncoast-LUG")
  private val BASE_URL   = "https://api.meetup.com" ? ("key" -> KEY)


  private val EVENTS_URL = (
      (BASE_URL / "2" / "events") ? ("text_format" -> "plain") 
                                  & ("group_urlname" -> GROUP_NAME))
  implicit val venueReads = Json.reads[Venue]
  implicit val eventReads = Json.reads[Event]
  
  def events() = Action {
    request => Ok(views.html.meetup.events(request))
  }

  def eventsText() = Action {
    Async {
      val time = DateTime.now.millis + "," + (DateTime.now + 4.weeks).millis
      WS.url(EVENTS_URL ? ("time" -> time)).get().map { response => 
        try {
          val events = (response.json \ "results").as[List[Event]]
          Ok(views.txt.meetup.events(events))
        }

        catch {
          case jsError : JsResultException => ( InternalServerError("An error occured processing the API data from meetup") )
        }
      }
    }
  }
}
