package home

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action { Ok("It works!") }
}
