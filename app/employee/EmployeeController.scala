package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import utils.ApiError


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
  def create = Action.async(parse.json) { request =>
    request.body.validate[CreateEmployeeDto].fold(
      errors => Future.successful(ApiError.InvalidJson(JsError(errors)).toResult),
      dto => service.createEmployee(dto).map {
        case Right(response) => Created(Json.toJson(response))
        case Left(error)     => error.toResult
      }
    )

  }

}
