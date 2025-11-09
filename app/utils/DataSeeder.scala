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

    val alice = Employee(None, "Alice",  "Smith",     "alice@example.com",   Some("0123456789"), Some("123 Main St"),      nowTs, nowTs)
    val pablo = Employee(None, "Pablo",  "Rial",      "pr@example.com",      Some("12547855"),   Some("123 Main Rd"),      nowTs, nowTs)
    val bob   = Employee(None, "Bob",    "Johnson",   "bob@example.com",     Some("0987654321"), Some("456 Elm St"),       nowTs, nowTs)
    val peter = Employee(None, "Peter",  "Capusotto", "pc@example.com",      Some("787878787"),  Some("Jua Jua St"),       nowTs, nowTs)
    val greg  = Employee(None, "Greg",   "Wallace",   "greg@example.com",    Some("4578545"),    Some("123 Cancel St"),    nowTs, nowTs)
    val tony  = Employee(None, "Tony",   "Stark",     "tony.stark@stark.io", Some("0700000001"), Some("10880 Malibu Pt"),  nowTs, nowTs)
    val bruce = Employee(None, "Bruce",  "Wayne",     "bruce@wayneenterp.com",Some("0700000002"),Some("1007 Mountain Dr"), nowTs, nowTs)
    val ellen = Employee(None, "Ellen",  "Ripley",    "ellen.ripley@weyu.com",Some("0700000003"),Some("Nostromo Deck A"),  nowTs, nowTs)
    val marty = Employee(None, "Marty",  "McFly",     "marty@hillvalley.tv",  Some("0700000004"),Some("9303 Lyon Dr"),      nowTs, nowTs)
    val john  = Employee(None, "John",   "Wick",      "john.wick@continental.org",Some("0700000005"),Some("14th St"),     nowTs, nowTs)

    val returningId = EmployeeTable.employees.returning(EmployeeTable.employees.map(_.id))

    val seedAction = for {
      aliceId <- returningId += alice
      pabloId <- returningId += pablo
      bobId   <- returningId += bob
      peterId <- returningId += peter
      gregId  <- returningId += greg
      tonyId  <- returningId += tony
      bruceId <- returningId += bruce
      ellenId <- returningId += ellen
      martyId <- returningId += marty
      johnId  <- returningId += john

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
         employeeId    = aliceId,
         contractStart = Date.valueOf(today.toLocalDate.minusYears(1)),
         contractEnd   = Some(Date.valueOf(today.toLocalDate.minusMonths(6))),
         contractType  = "contract",
         contractTime  = "part_time",
         salary        = Some(22000L),
         hoursPerWeek  = Some(20),
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
         salary        = Some(30000L),
         hoursPerWeek  = Some(24),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = bobId,
         contractStart = today,
         contractEnd   = Some(Date.valueOf(today.toLocalDate.plusMonths(6))),
         contractType  = "contractor",
         contractTime  = "part_time",
         salary        = Some(18000L),
         hoursPerWeek  = Some(20),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = peterId,
         contractStart = Date.valueOf(today.toLocalDate.minusMonths(2)),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "full_time",
         salary        = Some(50000L),
         hoursPerWeek  = Some(40),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = gregId,
         contractStart = Date.valueOf(today.toLocalDate.minusYears(2)),
         contractEnd   = Some(Date.valueOf(today.toLocalDate.minusYears(1))),
         contractType  = "contract",
         contractTime  = "part_time",
         salary        = Some(22000L),
         hoursPerWeek  = Some(24),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),

       Contract(
         id            = None,
         employeeId    = tonyId,
         contractStart = Date.valueOf("2023-01-01"),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "full_time",
         salary        = Some(120000L),
         hoursPerWeek  = Some(40),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = bruceId,
         contractStart = Date.valueOf("2024-05-01"),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "part_time",
         salary        = Some(95000L),
         hoursPerWeek  = Some(30),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = ellenId,
         contractStart = Date.valueOf("2023-04-01"),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "full_time",
         salary        = Some(52000L),
         hoursPerWeek  = Some(40),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = martyId,
         contractStart = Date.valueOf("1985-10-26"),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "part_time",
         salary        = Some(15000L),
         hoursPerWeek  = Some(16),
         createdAt     = nowTs,
         updatedAt     = nowTs
       ),
       Contract(
         id            = None,
         employeeId    = johnId,
         contractStart = Date.valueOf("2022-01-01"),
         contractEnd   = None,
         contractType  = "permanent",
         contractTime  = "full_time",
         salary        = Some(90000L),
         hoursPerWeek  = Some(40),
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