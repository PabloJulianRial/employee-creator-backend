package employee

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class EmployeeController @Inject()(
                                    cc: ControllerComponents,
                                    service: EmployeeService
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllEmployees: Action[AnyContent] = Action.async {
    service.getAllEmployees().map(dtos => Ok(Json.toJson(dtos)))
  }

  def getEmployeeById(id: Long): Action[AnyContent] = Action.async {
    service.getEmployeeById(id).map {
      case Right(dto)  => Ok(Json.toJson(dto))
      case Left(error) => error.toResult
    }
  }

  def createEmployee: Action[JsValue] = Action.async(parse.json) { req =>
    req.body.validate[CreateEmployeeDto].fold(
      errs => scala.concurrent.Future.successful(
        utils.validation.ApiError.InvalidJson(JsError(errs)).toResult
      ),
      dto  => service.createEmployee(dto).map {
        case Right(empDto) => Created(Json.toJson(empDto))
        case Left(err)     => err.toResult
      }
    )
  }

  def updateEmployee(id: Long): Action[JsValue] = Action.async(parse.json) { req =>
    req.body.validate[UpdateEmployeeDto].fold(
      errs => Future.successful(utils.validation.ApiError.InvalidJson(JsError(errs)).toResult),
      dto  => service.updateEmployeeById(id, dto).map {
        case Right(dtoOut) => Ok(Json.toJson(dtoOut))
        case Left(err)     => err.toResult
      }
    )
  }






}
