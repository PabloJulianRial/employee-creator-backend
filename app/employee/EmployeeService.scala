package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import contract.ContractRepository
import utils.validation.ApiError
import java.sql.Timestamp
import java.time.Instant
import contract.ContractResponse



@Singleton
class EmployeeService @Inject()(
                                 employeeRepository: EmployeeRepository,
                                 contractRepository: ContractRepository
                               )(implicit ec: ExecutionContext) {

  def getAllEmployees(): Future[Seq[EmployeeResponse]] = {
    employeeRepository.findAll().flatMap { employees =>
      val futures: Seq[Future[EmployeeResponse]] = employees.map { e =>
        val id = e.id.getOrElse(0L)
        contractRepository.findByEmployeeId(id).map {
          case c +: _ => EmployeeResponse.fromModels(e, c)
          case _ => EmployeeResponse.fromModel(e)
        }
      }
      Future.sequence(futures)
    }
  }

  def getEmployeeById(id: Long): Future[Either[ApiError, EmployeeResponse]] = {
    employeeRepository.findById(id).flatMap {
      case None =>
        Future.successful(Left(ApiError.NotFound(s"Employee with id $id not found")))
      case Some(emp) =>
        val empId = emp.id.getOrElse(0L)
        contractRepository.findByEmployeeId(empId).map {
          case c +: _ => Right(EmployeeResponse.fromModels(emp, c))
          case _ => Right(EmployeeResponse.fromModel(emp))
        }
    }
  }

  def createEmployee(data: CreateEmployeeDto): Future[Either[ApiError, EmployeeResponse]] = {
    val errors = EmployeeValidator.validateCreate(data);
    if (errors.nonEmpty) {
      Future.successful(Left(ApiError.ValidationError(errors)))
    } else {
      val now = Timestamp.from(Instant.now())
      val preSaved = Employee(
        id = None,
        firstName = data.firstName.trim,
        lastName = data.lastName.trim,
        email = data.email.trim,
        mobile = data.mobileNumber,
        address = data.address,
        createdAt = now,
        updatedAt = now
      )
      employeeRepository.create(preSaved).map(saved => Right(EmployeeResponse.fromModel(saved)))
    }

  }

  def updateEmployeeById(id: Long, data: UpdateEmployeeDto): Future[Either[ApiError, EmployeeResponse]] = {
    val errors = EmployeeValidator.validatePatch(data)
    if (errors.nonEmpty) {
      Future.successful(Left(ApiError.ValidationError(errors)))
    } else {
      employeeRepository.findById(id).flatMap {
        case None =>
          Future.successful(Left(ApiError.NotFound(s"Employee with id $id not found")))
        case Some(existing) =>
          val now = Timestamp.from(Instant.now())
          val updated = existing.copy(
            firstName     = data.firstName   .getOrElse(existing.firstName),
            lastName      = data.lastName    .getOrElse(existing.lastName),
            email         = data.email       .getOrElse(existing.email),
            mobile        = data.mobileNumber.orElse(existing.mobile),
            address       = data.address     .orElse(existing.address),
            updatedAt     = now
          )

          employeeRepository.update(updated).map(c => Right(EmployeeResponse.fromModel(c)))
          }
      }
    }

  def deleteEmployeeById(id: Long): Future[Either[ApiError, Unit]] = {
    employeeRepository.delete(id).map { rowsAffected =>
      if (rowsAffected > 0) Right(())
      else Left(ApiError.NotFound(s"Employee with id $id not found"))
    }
  }

  def getAllEmployeeContracts(id: Long): Future[Seq[ContractResponse]] = {
    contractRepository.findByEmployeeId(id).map(_.map(ContractResponse.fromModel))
  }

}
