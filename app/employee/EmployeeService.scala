package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import contract.ContractRepository
import utils.validation.ApiError

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
          case Some(c) => EmployeeResponse.fromModels(e, c)
          case None    => EmployeeResponse.fromModel(e)
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
        contractRepository.findByEmployeeId(emp.id.getOrElse(0L)).map {
          case Some(c) => Right(EmployeeResponse.fromModels(emp, c))
          case None    => Right(EmployeeResponse.fromModel(emp))
        }
    }
  }



}
