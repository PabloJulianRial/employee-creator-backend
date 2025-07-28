package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class EmployeeController @Inject()(
                                    cc: ControllerComponents,
                                    service: EmployeeService
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllEmployees: Action[AnyContent] = Action.async {
    service.getAllEmployees().map { dtos =>
      Ok(Json.toJson(dtos))
    }
  }

  def getEmployeeById(id: Long): Action[AnyContent] = Action.async {
    service.getEmployeeById(id).map {
      case Right(dto)  => Ok(Json.toJson(dto))
      case Left(error) => error.toResult
    }
  }
}
