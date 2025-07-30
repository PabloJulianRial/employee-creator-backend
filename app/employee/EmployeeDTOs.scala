package employee

import play.api.libs.json._

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

  def fromModel(model: Employee): EmployeeResponse =
    EmployeeResponse(
      id            = model.id.getOrElse(0L),
      firstName     = model.firstName,
      lastName      = model.lastName,
      email         = model.email,
      mobileNumber  = model.mobileNumber,
      address       = model.address,
      contractStart = model.contractStart.toString,
      contractType  = model.contractType,
      contractTime  = model.contractTime,
      contractEnd   = model.contractEnd.map(_.toString),
      hoursPerWeek  = model.hoursPerWeek,
      createdAt     = model.createdAt.toString,
      updatedAt     = model.updatedAt.toString
    )
}

case class CreateEmployeeDto(
                              firstName: String,
                              lastName: String,
                              email: String,
                              mobileNumber: Option[String],
                              address: Option[String],
                              contractStart: String,
                              contractType: String,
                              contractTime: String,
                              contractEnd: Option[String],
                              hoursPerWeek: Int
                            )

object CreateEmployeeDto {
  implicit val reads: Reads[CreateEmployeeDto] = Json.reads[CreateEmployeeDto]
}
case class UpdateEmployeeDto(
                              firstName: Option[String],
                              lastName: Option[String],
                              email: Option[String],
                              mobileNumber: Option[String],
                              address: Option[String],
                              contractStart: Option[String],
                              contractType: Option[String],
                              contractTime: Option[String],
                              contractEnd: Option[String],
                              hoursPerWeek: Option[Int]
                            )

object UpdateEmployeeDto {
  implicit val reads: Reads[UpdateEmployeeDto] = Json.reads[UpdateEmployeeDto]
}