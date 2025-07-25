package utils

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import java.sql.Timestamp
import java.time.LocalDateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.employee.{Employee, EmployeeTable}
import models.contract.{Contract, ContractTable}
import models.employee.EmployeeTable.employees

@Singleton
class DataSeeder @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  def seed(): Future[Unit] = {
    val now = Timestamp.valueOf(LocalDateTime.now())

    val initialEmployees = Seq(
      Employee(None, "Alice", "Smith", "alice@example.com", "1234567890", "1 Main St"),
      Employee(None, "Bob",   "Jones", "bob@example.com",   "0987654321", "2 Main St")
    )

    val initialContracts = Seq(
      Contract(None, 1, now, None, "permanent", "full time",  40),
      Contract(None, 2, now, None, "contract",  "part time", 20)
    )

    val insertIfEmpty = for {
      exists <- employees.exists.result
      _ <- if (!exists) employees ++= initialEmployees else DBIO.successful(())
    } yield ()

    db.run(insertIfEmpty.transactionally)
  }
}
