package models.employee

import slick.jdbc.MySQLProfile.api._

class Employees(tag: Tag) extends Table[Employee](tag, "employees") {
  def id        = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName  = column[String]("last_name")
  def email     = column[String]("email")
  def mobile    = column[String]("mobile")
  def address   = column[String]("address")

  def * = (id.?, firstName, lastName, email, mobile, address) <> (Employee.tupled, Employee.unapply)
}

object EmployeeTable {
  val employees = TableQuery[Employees]
}
