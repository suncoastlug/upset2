package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import models.meetup._
import com.github.nscala_time.time.Imports._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class MeetupSpec extends Specification {
  
  "Meetup" should {
    
    "render the events as plain text" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/meetup/events.txt")).get
        
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/plain")
        contentAsString(home) must contain ("Suncoast Linux Users Group")
      }
    }
  }

  // "Meetup EventCalendar" should {
  //   val cal = new EventCalendar(DateTime.now, List())

  //   "know first day of week is Friday" in {
  //     cal.firstDate.getDayOfWeek() must equalTo(5)
  //   }

  //   "know the last day of week is Saturday" in {
  //     cal.lastDate.getDayOfWeek must equalTo(6)
  //   }
  // }
}

