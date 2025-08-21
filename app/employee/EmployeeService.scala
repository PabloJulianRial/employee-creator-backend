package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import contract.ContractRepository

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

}
