package utils

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import employee.{Table => EmployeeTable, Employee}
import contract.{Table => ContractTable, Contract}
import java.sql.{Date, Timestamp}
import java.time.LocalDate

@Singleton
class DataSeeder @Inject()(
                            dbConfigProvider: DatabaseConfigProvider
                          )(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  def seed(): Future[Unit] = {
    val nowTs  = new Timestamp(System.currentTimeMillis())
    val today  = Date.valueOf(LocalDate.now())

    val alice = Employee(None, "Alice", "Smith",    "alice@example.com", Some("0123456789"), Some("123 Main St"),   nowTs, nowTs)
    val pablo = Employee(None, "Pablo", "Rial",     "PR@example.com",    Some("12547855"),  Some("123 Main Rd"),   nowTs, nowTs)
    val bob   = Employee(None, "Bob",   "Johnson",  "bob@example.com",   Some("0987654321"),Some("456 Elm St"),    nowTs, nowTs)
    val peter = Employee(None, "Peter", "Capusotto","PC@example.com",    Some("787878787"), Some("Jua Jua St"),    nowTs, nowTs)
    val greg  = Employee(None, "Greg",  "Wallace",  "greg@example.com",  Some("4578545"),   Some("123 Cancel St"), nowTs, nowTs)

    val returningId = EmployeeTable.employees.returning(EmployeeTable.employees.map(_.id))

    val seedAction = for {
      aliceId <- returningId += alice
      pabloId <- returningId += pablo
      bobId   <- returningId += bob

      _ <- EmployeeTable.employees ++= Seq(peter, greg)

      _ <- ContractTable.contracts ++= Seq(
        Contract(
          id            = None,
          employeeId    = aliceId,
          contractStart = today,
          contractEnd   = None,
          contractType  = "permanent",
          contractTime  = "full_time",
          salary        = Some(45000L),
          hoursPerWeek  = Some(40),
          createdAt     = nowTs,
          updatedAt     = nowTs
        ),
        Contract(
          id            = None,
          employeeId    = pabloId,
          contractStart = today,
          contractEnd   = None,
          contractType  = "permanent",
          contractTime  = "part_time",
          salary        = Some(22000L),
          hoursPerWeek  = Some(20),
          createdAt     = nowTs,
          updatedAt     = nowTs
        ),
        Contract(
          id            = None,
          employeeId    = bobId,
          contractStart = today,
          contractEnd   = Some(Date.valueOf(LocalDate.now().plusMonths(6))),
          contractType  = "contractor",
          contractTime  = "part_time",
          salary        = Some(18000L),
          hoursPerWeek  = Some(20),
          createdAt     = nowTs,
          updatedAt     = nowTs
        )
      )
    } yield ()

    val setup = for {
      exists <- EmployeeTable.employees.exists.result
      _      <- if (!exists) seedAction else DBIO.successful(())
    } yield ()

    db.run(setup.transactionally)
  }
}
