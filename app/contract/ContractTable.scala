package contract

import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape
import java.sql.{Date, Timestamp}

class Contracts(tag: Tag) extends Table[Contract](tag, "contracts") {
  def id            = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def employeeId    = column[Long]("employee_id")
  def contractStart = column[Date]("contract_start")
  def contractEnd   = column[Option[Date]]("contract_end")
  def contractType  = column[String]("contract_type")
  def contractTime  = column[String]("contract_time")
  def salary        = column[Option[Long]]("salary")
  def hoursPerWeek  = column[Option[Int]]("hours_per_week")
  def createdAt     = column[Timestamp]("created_at")
  def updatedAt     = column[Timestamp]("updated_at")

  def * : ProvenShape[Contract] =
    (id.?, employeeId, contractStart, contractEnd, contractType, contractTime, salary, hoursPerWeek, createdAt, updatedAt)
      .<>(Contract.tupled, Contract.unapply)
}

object Table {
  val contracts = TableQuery[Contracts]
}
