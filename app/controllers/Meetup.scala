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

import com.github.theon.uri.dsl._
import models.meetup._
import com.github.nscala_time.time.Imports._

object Meetup extends Controller {
  private val MEETUP_KEY   = current.configuration.getString("meetup.key")
  private val MEETUP_GROUP = current.configuration.getString("meetup.group").getOrElse("Suncoast-LUG")
  private val MEETUP_URL   = current.configuration.getString("meetup.url").getOrElse("https://api.meetup.com")
  private val EVENTS_URL   = (MEETUP_URL / "2" / "events") ? ("text_format" -> "plain")
                                                           & ("key" -> MEETUP_KEY)
                                                           & ("group_urlname" -> MEETUP_GROUP)

  implicit val venueReads = Json.reads[Venue]
  implicit val eventReads = Json.reads[Event]

  private def getEvents(start: DateTime, end: DateTime) =
    WS.url(EVENTS_URL ? ("time" -> (start.millis + "," + end.millis))).get.map { response =>
      (response.json \ "results").asOpt[List[Event]]
    }


  def eventsText = Action.async {
    getEvents(DateTime.now, DateTime.now + 5.weeks).map {
      case Some(events) => Ok(views.txt.meetup.events(events))
      case None         => InternalServerError("Failed to parse response from meetup events API")
    }
  }
}
