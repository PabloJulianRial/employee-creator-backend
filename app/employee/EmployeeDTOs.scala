package employee

import play.api.libs.json._
import _root_.contract.{Contract => ContractModel}

case class EmployeeResponse(
                             id: Long,
                             firstName: String,
                             lastName: String,
                             email: String,
                             mobileNumber: Option[String],
                             address: Option[String],
                             contractStart: String,
                             contractType: String,
                             contractTime: String,
                             contractEnd: Option[String],
                             hoursPerWeek: Int,
                             createdAt: String,
                             updatedAt: String
                           )

object EmployeeResponse {
  implicit val format: OFormat[EmployeeResponse] = Json.format[EmployeeResponse]

  def fromModels(e: Employee, c: ContractModel): EmployeeResponse =
    EmployeeResponse(
      id            = e.id.getOrElse(0L),
      firstName     = e.firstName,
      lastName      = e.lastName,
      email         = e.email,
      mobileNumber  = e.mobile,
      address       = e.address,
      contractStart = c.contractStart.toString,
      contractType  = c.contractType,
      contractTime  = c.contractTime,
      contractEnd   = c.contractEnd.map(_.toString),
      hoursPerWeek  = c.hoursPerWeek.getOrElse(0),
      createdAt     = e.createdAt.toString,
      updatedAt     = e.updatedAt.toString
    )

  def fromModel(employee: Employee): EmployeeResponse =
    EmployeeResponse(
      id            = employee.id.getOrElse(0L),
      firstName     = employee.firstName,
      lastName      = employee.lastName,
      email         = employee.email,
      mobileNumber  = employee.mobile,
      address       = employee.address,
      contractStart = "",
      contractType  = "",
      contractTime  = "",
      contractEnd   = None,
      hoursPerWeek  = 0,
      createdAt = employee.createdAt.toString,
      updatedAt = employee.updatedAt.toString

    )
}

case class CreateEmployeeDto(
                              firstName: String,
                              lastName: String,
                              email: String,
                              mobileNumber: Option[String],
                              address: Option[String]
                            )
object CreateEmployeeDto {
  implicit val reads: Reads[CreateEmployeeDto] = Json.reads[CreateEmployeeDto]
}

case class UpdateEmployeeDto(
                              firstName: Option[String],
                              lastName: Option[String],
                              email: Option[String],
                              mobileNumber: Option[String],
                              address: Option[String]
                            )
object UpdateEmployeeDto {
  implicit val reads: Reads[UpdateEmployeeDto] = Json.reads[UpdateEmployeeDto]
}
