package employee

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import contract.CreateContractDto
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

  def deleteEmployee(id: Long): Action[AnyContent] = Action.async {
    service.deleteEmployeeById(id).map {
      case Right(_)     => NoContent
      case Left(error)  => error.toResult
    }
  }

  def getEmployeeContracts(id: Long): Action[AnyContent] = Action.async {
    service.getAllEmployeeContracts(id).map { dtos =>
      Ok(Json.toJson(dtos))
    }
  }

  def createEmployeeContract(id: Long): Action[JsValue] = Action.async(parse.json) { req =>
    req.body.validate[CreateContractDto].fold(
      errs => Future.successful(utils.validation.ApiError.InvalidJson(JsError(errs)).toResult),
      dto  => service.createContractForEmployee(id, dto).map {
        case Right(out) => Created(Json.toJson(out))
        case Left(err)  => err.toResult
      }
    )
  }

  def deleteEmployeeContract(empId: Long, contractId: Long): Action[AnyContent] = Action.async {
    service.deleteContractForEmployee(empId, contractId).map {
      case Right(_)   => NoContent
      case Left(err)  => err.toResult
    }
  }

  def getEmployeeContract(empId: Long, contractId: Long): Action[AnyContent] = Action.async {
    service.getContractForEmployee(empId, contractId).map {
      case Right(dto) => Ok(Json.toJson(dto))
      case Left(err)  => err.toResult
    }
  }



}
