package employee

import java.sql.{Date, Timestamp}
import slick.jdbc.MySQLProfile.api._

class Employees(tag: Tag) extends Table[Employee](tag, "employees") {
  def id             = column[Long]         ("id", O.PrimaryKey, O.AutoInc)
  def firstName      = column[String]       ("first_name")
  def lastName       = column[String]       ("last_name")
  def email          = column[String]       ("email")
  def mobileNumber   = column[Option[String]]("mobile_number")
  def address        = column[Option[String]]("address")
  def contractStart  = column[Date]         ("contract_start")
  def contractType   = column[String]       ("contract_type")
  def contractTime   = column[String]       ("full_time")
  def contractEnd    = column[Option[Date]] ("contract_end")
  def hoursPerWeek   = column[Int]          ("hours_per_week")
  def createdAt      = column[Timestamp]    ("created_at")
  def updatedAt      = column[Timestamp]    ("updated_at")


  def * = (
    id.?, firstName, lastName, email, mobileNumber, address,
    contractStart, contractType, contractTime, contractEnd,
    hoursPerWeek, createdAt, updatedAt
  ) <> (Employee.tupled, Employee.unapply)
}

object Table {
  val employees = TableQuery[Employees]
}
