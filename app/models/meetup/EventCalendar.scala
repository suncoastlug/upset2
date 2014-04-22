package models.meetup

import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime


class EventCalendar(calendarEvents: List[Event]) {
  case class Node(date: DateTime) {
    val nodeEvents = calendarEvents.filter(date==_.startTime.toString("Y-M-d"))
  }

  private val now: DateTime = 
    calendarEvents.headOption.map(_.startTime).getOrElse(DateTime.now())

  // figure out the first and last days of the month
  private val firstDate = now.withDayOfMonth(1).withTime(0, 0, 0, 0)
  private val lastDate  = (firstDate + 1.months) - 1.days

  // get all days in month as range.
  private val days = firstDate.getDayOfMonth to lastDate.getDayOfMonth

  // get all dates in month
  private val dates = for (day <- days) yield firstDate + (day-1).days

  val weeks: Seq[Seq[Option[Node]]] = 
    dates.groupBy(_.getWeekOfWeekyear)  // group by week number
         .toSeq.sortBy(_._1).map(_._2)  // sort by and discard week number 
         .map { _.groupBy(_.getDayOfWeek).mapValues(_.head) }
         .map { x => for (n <- 1 to 7) yield x.get(n).map(Node) }


}

