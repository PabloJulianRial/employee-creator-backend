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
}
