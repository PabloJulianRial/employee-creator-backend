package employee

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext

@Singleton
class EmployeeController @Inject()(
                                    cc: ControllerComponents,
                                    service: EmployeeService
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllEmployees: Action[AnyContent] = Action.async {
    service.getAllEmployees().map(rows => Ok(Json.toJson(rows)))
  }
}
