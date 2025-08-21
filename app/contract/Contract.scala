package contract

import java.sql.{Date, Timestamp}

case class Contract(
                     id: Option[Long],
                     employeeId: Long,
                     contractStart: Date,
                     contractEnd: Option[Date],
                     contractType: String,
                     contractTime: String,
                     salary: Option[Long],
                     hoursPerWeek: Option[Int],
                     createdAt: Timestamp,
                     updatedAt: Timestamp
                   )
