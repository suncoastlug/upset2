package models.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.lib.Repository

import org.gitective.core.BlobUtils

import java.io.File

object GitRepository {

  def apply(directory: File) = new GitRepository(new RepositoryBuilder().setGitDir(directory).build())

  def clone(uri: String, directory: File): GitRepository = {
    val repo = Git.cloneRepository
      .setURI(uri)
      .setBare(true)
      .setCloneAllBranches(true)
      .setDirectory(directory)
      .call()
      .getRepository()
    return new GitRepository(repo)
  }

}

class GitRepository private[git] (repository: Repository) {

  private lazy val git = new Git(repository) 
  
  def getContent(path: String, branch: String): Option[String] =
    Option(BlobUtils.getContent(repository, branch, path))

  def fetch() = git.fetch.call()

}
