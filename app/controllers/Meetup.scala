package controllers

import play.Logger
import play.api.Play.current
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Promise
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.templates.Html
import scala.concurrent.Future
import scala.concurrent.duration._

import com.netaporter.uri.dsl._
import models.meetup._
import com.github.nscala_time.time.Imports._

object Meetup extends Controller {
  private val MEETUP_KEY   = current.configuration.getString("meetup.key")
  private val MEETUP_GROUP = current.configuration.getString("meetup.group").getOrElse("Suncoast-LUG")
  private val MEETUP_URL   = current.configuration.getString("meetup.url").getOrElse("https://api.meetup.com")

  // We define some URLs using com.netaporter.uri.dsl implicits, which provide the / ? & sugar.
  private val EVENTS_URL   = ((MEETUP_URL / "2" / "events") ? ("text_format"   -> "plain")
                                                            & ("key"           -> MEETUP_KEY)
                                                            & ("group_urlname" -> MEETUP_GROUP))

  private def getEvents(start: DateTime, end: DateTime): Future[Option[List[Event]]] = {
    val events_url = EVENTS_URL ? ("time" -> (start.millis + "," + end.millis))

    WS.url(events_url).withRequestTimeout(1000).get.map { response =>
      implicit val venueReads = Json.reads[Venue]
      implicit val eventReads = Json.reads[Event]
      val json                = response.json

      (json \ "results").asOpt[List[Event]]
    }
  }

  def eventsText = Action.async {
    getEvents(DateTime.now, DateTime.now + 5.weeks).map {
      case Some(events) => Ok(views.txt.meetup.events(events))
      case None         => InternalServerError("Failed to parse response from meetup events API")
    }
  }

//   def calendar = Action.async {
//     getEvents(DateTime.now, DateTime.now + 5.weeks) map {
//       case Some(events) => Ok(views.html.meetup.calendar(
//       case None         => j
//     }
//   }
}
