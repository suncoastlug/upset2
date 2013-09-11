package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import java.io.File
import java.nio.file.Files
import org.gitective.core.BlobUtils


import models.Page

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class PageSpec extends Specification {
  val markdown = """
# page title

Some other text.
"""
  val markdownNoTitle = """
This page has no title
"""


  "Page" should {
    "title" in {
      Page.parse(markdown).title must_== "page title"
      Page.parse(markdownNoTitle, "untitled").title must_== "untitled"
    }

    "html" in {
      Page.parse(markdown).html.body must contain("Some other text")
    }

    "markdown" in {
      Page.parse(markdown).markdown must be equalTo(markdown)
    }

  }
}
