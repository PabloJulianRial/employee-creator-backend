package contract

import play.api.libs.json._

case class ContractResponse(
                             id: Long,
                             employeeId: Long,
                             contractStart: String,
                             contractEnd: Option[String],
                             contractType: String,
                             contractTime: String,
                             salary: Option[Long],
                             hoursPerWeek: Option[Int],
                             createdAt: String,
                             updatedAt: String
                           )

object ContractResponse {
  implicit val format: OFormat[ContractResponse] = Json.format[ContractResponse]

  def fromModel(c: Contract): ContractResponse =
    ContractResponse(
      id            = c.id.getOrElse(0L),
      employeeId    = c.employeeId,
      contractStart = c.contractStart.toString,
      contractEnd   = c.contractEnd.map(_.toString),
      contractType  = c.contractType,
      contractTime  = c.contractTime,
      salary        = c.salary,
      hoursPerWeek  = c.hoursPerWeek,
      createdAt     = c.createdAt.toString,
      updatedAt     = c.updatedAt.toString
    )
}
case class CreateContractDto(
                              contractStart: String,
                              contractEnd: Option[String],
                              contractType: String,
                              contractTime: String,
                              salary: Option[Long],
                              hoursPerWeek: Option[Int]
                            )
object CreateContractDto {
  implicit val reads: Reads[CreateContractDto] = Json.reads[CreateContractDto]
}
