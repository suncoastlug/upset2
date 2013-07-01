package models.git

import org.eclipse.jgit.api.CloneCommand
import java.io.File
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository
import org.gitective.core.BlobUtils

object GitRepository {
  
  def apply(directory: File) = new GitRepository(new FileRepository(directory))

  def clone(uri: String, directory: File): GitRepository = {
    val repo = new CloneCommand()
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
  
  def getContent(path: String, branch: String = "master"): Option[String] =
    Option(BlobUtils.getContent(repository, branch, path))
    
  def isBare() = repository.isBare()


  
  

  
  
  
}
