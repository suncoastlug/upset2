package upset

import play.api._
import java.io.File

object Globals extends GlobalSettings {
   
  override def onStart(app: Application) = {
    controllers.Git.initialize
  }

}
