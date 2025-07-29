package employee

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
class EmployeeRepository @Inject()(
                                    dbConfigProvider: DatabaseConfigProvider
                                  )(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val employees = Table.employees

  def findAll(): Future[Seq[Employee]] =
    db.run(employees.result)

  def findById(id: Long): Future[Option[Employee]] =
    db.run(employees.filter(_.id === id).result.headOption)

  def create(employee: Employee): Future[Employee] = {
    val insertQuery =
      employees returning employees.map(_.id) into((employee, id) => employee.copy(id = Some(id)))

    db.run(insertQuery += employee)
  }
}
