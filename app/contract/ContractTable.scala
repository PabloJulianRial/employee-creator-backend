package models.contract

import slick.jdbc.MySQLProfile.api._
import java.sql.Timestamp
import models.employee.EmployeeTable

class Contracts(tag: Tag) extends Table[Contract](tag, "contracts") {
  def id           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def employeeId   = column[Long]("employee_id")
  def startDate    = column[Timestamp]("start_date")
  def endDate      = column[Option[Timestamp]]("end_date")
  def contractType = column[String]("contract_type")
  def contractTime = column[String]("contract_time")
  def hoursWeek    = column[Int]("hours_week")


  def fkEmp = foreignKey("fk_contract_emp", employeeId, EmployeeTable.employees)(_.id, onDelete = ForeignKeyAction.Cascade)

  def * = (
    id.?, employeeId, startDate, endDate,
    contractType, contractTime, hoursWeek
  ) <> (Contract.tupled, Contract.unapply)
}

object ContractTable {
  val contracts = TableQuery[Contracts]
}
