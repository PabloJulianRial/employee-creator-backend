package employee

import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape
import java.sql.Timestamp

class Employees(tag: Tag) extends Table[Employee](tag, "employees") {
  def id        = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName  = column[String]("last_name")
  def email     = column[String]("email")
  def mobile    = column[Option[String]]("mobile")
  def address   = column[Option[String]]("address")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")

  def * : ProvenShape[Employee] =
    (id.?, firstName, lastName, email, mobile, address, createdAt, updatedAt)
      .<>(Employee.tupled, Employee.unapply)
}

object Table {
  val employees = TableQuery[Employees]
}
