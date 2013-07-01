package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import models.git.GitRepository
import java.io.File
import java.nio.file.Files
import org.gitective.core.BlobUtils

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class GitRepositorySpec extends Specification {
  val dir = Files.createTempDirectory("slug-pages")


  "GitRepository" should {
    "clone" in {
      val repo = GitRepository.clone("git://github.com/suncoastlug/slug-pages.git", dir.toFile)
        repo.isBare() must beTrue
    }

    "apply" in {
      val repo = GitRepository(dir.toFile)
      repo.isBare() must beTrue

    }

    "get file content" in {
      val repo = GitRepository(dir.toFile)
      val content : String = repo.getContent("README.md", "master").getOrElse("")

      content must contain("slug-pages")
    }




    }
  }
