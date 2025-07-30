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

  def update(employee: Employee): Future[Employee] = {
    val query = employees.filter(_.id === employee.id.get)
      .map(c => (
        c.firstName, c.lastName, c.email,
        c.mobileNumber, c.address,
        c.contractStart, c.contractType, c.contractTime,
        c.contractEnd, c.hoursPerWeek, c.updatedAt
      ))
      .update((
        employee.firstName, employee.lastName, employee.email,
        employee.mobileNumber, employee.address,
        employee.contractStart, employee.contractType, employee.contractTime,
        employee.contractEnd, employee.hoursPerWeek, employee.updatedAt
      ))

    db.run(query).map(_ => employee)
  }
  def delete(id: Long): Future[Int] = {
    db.run(employees.filter(_.id === id).delete)
  }

}
