package models.meetup

import play.api.libs.json._

import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.Duration

case class Venue(
  address_1: Option[String],
  address_2: Option[String],
  address_3: Option[String],
  city: Option[String],
  state: Option[String],
  country: Option[String],
  name: String,
  phone: Option[String],
  zip: Option[String]
) {
  lazy val address = List(address_1, address_2, address_3).flatten

}

case class Event(
  name: String, 
  time: Long, 
  venue: Option[Venue],
  why: Option[String],
  how_to_find_us: Option[String],
  event_url: String,
  description: String,
  duration: Option[Long],
  announced: Option[Boolean],
  maybe_rsvp_count: Int,
  yes_rsvp_count: Int

) {
  private val startFmt = 
    new DateTimeFormatterBuilder()
      .appendDayOfWeekText()
      .appendLiteral(", ")
      .appendMonthOfYearText()
      .appendLiteral(" ")
      .appendDayOfMonth(1)
      .appendLiteral(", ")
      .appendYear(4, 4)
      .appendLiteral(" @ ")
      .appendClockhourOfHalfday(2)
      .appendLiteral(':')
      .appendMinuteOfHour(2)
      .appendHalfdayOfDayText()
      .toFormatter();
  private val endFmt = new DateTimeFormatterBuilder()
      .appendClockhourOfHalfday(2)
      .appendLiteral(':')
      .appendMinuteOfHour(2)
      .appendHalfdayOfDayText()
      .toFormatter();

  val startTime = new DateTime(time)
  val endTime   = startTime + new Duration(duration.getOrElse(3600000.toLong).toLong)

  val startText = startTime.toString(startFmt)
  val endText = endTime.toString(endFmt)

  val wrapRegEx = """(.{1,76})\s""".r
  
  lazy val desc = wrapRegEx.replaceAllIn(description, m=>m.group(1)+"\n").split("\n").map(line => "    " + line).mkString("\n")
}

  


