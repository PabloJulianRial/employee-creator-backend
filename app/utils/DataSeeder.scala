package utils
import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import employee.{Table => EmployeeTable, Employee}
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
    val nowTs = new Timestamp(System.currentTimeMillis())
    val today = Date.valueOf(LocalDate.now())

    val sampleEmployees = Seq(
      Employee(
        id            = None,
        firstName     = "Alice",
        lastName      = "Smith",
        email         = "alice@example.com",
        mobileNumber  = Some("0123456789"),
        address       = Some("123 Main St"),
        contractStart = today,
        contractType  = "permanent",
        contractTime  = "full-time",
        contractEnd   = None,
        hoursPerWeek  = 40,
        createdAt     = nowTs,
        updatedAt     = nowTs
      ),Employee(
        id            = None,
        firstName     = "Peter",
        lastName      = "Capusotto",
        email         = "PC@example.com",
        mobileNumber  = Some("787878787"),
        address       = Some("Jua Jua St"),
        contractStart = today,
        contractType  = "permanent",
        contractTime  = "full-time",
        contractEnd   = None,
        hoursPerWeek  = 40,
        createdAt     = nowTs,
        updatedAt     = nowTs
      ),Employee(
        id            = None,
        firstName     = "Greg",
        lastName      = "Wallace",
        email         = "alice@example.com",
        mobileNumber  = Some("4578545"),
        address       = Some("123 Cancel St"),
        contractStart = today,
        contractType  = "permanent",
        contractTime  = "full-time",
        contractEnd   = None,
        hoursPerWeek  = 40,
        createdAt     = nowTs,
        updatedAt     = nowTs
      ),Employee(
        id            = None,
        firstName     = "Pablo",
        lastName      = "Rial",
        email         = "PR@example.com",
        mobileNumber  = Some("12547855"),
        address       = Some("123 Main Rd"),
        contractStart = today,
        contractType  = "permanent",
        contractTime  = "part-time",
        contractEnd   = None,
        hoursPerWeek  = 40,
        createdAt     = nowTs,
        updatedAt     = nowTs
      ),
      Employee(
        id            = None,
        firstName     = "Bob",
        lastName      = "Johnson",
        email         = "bob@example.com",
        mobileNumber  = Some("0987654321"),
        address       = Some("456 Elm St"),
        contractStart = today,
        contractType  = "contract",
        contractTime  = "part-time",
        contractEnd   = Some(Date.valueOf(LocalDate.now().plusMonths(6))),
        hoursPerWeek  = 20,
        createdAt     = nowTs,
        updatedAt     = nowTs
      )
    )

    val setup = for {
      exists <- EmployeeTable.employees.exists.result
      _      <- if (!exists) EmployeeTable.employees ++= sampleEmployees
      else DBIO.successful(())
    } yield ()

    db.run(setup.transactionally)
  }
}
