package employee

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmployeeRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val employees = Table.employees

  def findAll(): Future[Seq[Employee]] =
    db.run(employees.result)

  def findById(id: Long): Future[Option[Employee]] =
    db.run(employees.filter(_.id === id).result.headOption)

  def create(employee: Employee): Future[Either[utils.validation.ApiError, Long]] = {
    val now = new java.sql.Timestamp(System.currentTimeMillis())
    val row = employee.copy(id = None, createdAt = now, updatedAt = now)

    val action = (employees returning employees.map(_.id)) += row

    db.run(action.asTry).map {
      case scala.util.Success(newId) => Right(newId)
      case scala.util.Failure(ex)    =>
         Left(utils.validation.ApiError.BadRequest(ex.getMessage))
    }
  }


}
