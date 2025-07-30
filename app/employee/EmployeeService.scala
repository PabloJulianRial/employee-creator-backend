package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import java.sql.{Date, Timestamp}
import java.time.Instant
import utils.ApiError

@Singleton
class EmployeeService @Inject()(
                                 employeeRepository: EmployeeRepository
                               )(implicit ec: ExecutionContext) {

  def getAllEmployees(): Future[Seq[EmployeeResponse]] =
    employeeRepository.findAll().map(_.map(EmployeeResponse.fromModel))

  def getEmployeeById(id: Long): Future[Either[ApiError, EmployeeResponse]] = {
    employeeRepository.findById(id).map {
      case Some(emp) => Right(EmployeeResponse.fromModel(emp))
      case None      => Left(ApiError.NotFound(s"Employee with id $id not found"))
    }
  }

  def createEmployee(data: CreateEmployeeDto): Future[Either[ApiError, EmployeeResponse]] = {
    val errors = EmployeeValidator.validateCreate(data);
    if (errors.nonEmpty) {
      Future.successful(Left(ApiError.ValidationError(errors)))
    } else {
      val now = Timestamp.from(Instant.now())
      val start = Date.valueOf(data.contractStart)
      val end = data.contractEnd.map(Date.valueOf)
      val preSaved = Employee(
        id = None,
        firstName = data.firstName.trim,
        lastName = data.lastName.trim,
        email = data.email.trim,
        mobileNumber = data.mobileNumber,
        address = data.address,
        contractStart = start,
        contractType = data.contractType.trim,
        contractTime = data.contractTime.trim,
        contractEnd = end,
        hoursPerWeek = data.hoursPerWeek,
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
            mobileNumber  = data.mobileNumber.orElse(existing.mobileNumber),
            address       = data.address     .orElse(existing.address),
            contractStart = data.contractStart.map(Date.valueOf).getOrElse(existing.contractStart),
            contractType  = data.contractType.getOrElse(existing.contractType),
            contractTime  = data.contractTime.getOrElse(existing.contractTime),
            contractEnd   = data.contractEnd   .map(Date.valueOf).orElse(existing.contractEnd),
            hoursPerWeek  = data.hoursPerWeek  .getOrElse(existing.hoursPerWeek),
            updatedAt     = now
          )

          employeeRepository.update(updated).map(c => Right(EmployeeResponse.fromModel(c)))
      }
    }
  }
  def deleteEmployeeById(id: Long): Future[Either[ApiError, Unit]] = {
    employeeRepository.delete(id).map { rowsAffected =>
      if (rowsAffected > 0) Right(())
      else Left(ApiError.NotFound(s"Category with id $id not found"))
    }
  }

}
