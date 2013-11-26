package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

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
}

