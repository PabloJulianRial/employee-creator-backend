package models.contract

import java.sql.Timestamp

case class Contract(
                     id:           Option[Long],
                     employeeId:   Long,
                     startDate:    Timestamp,
                     endDate:      Option[Timestamp],
                     contractType: String,
                     contractTime: String,
                     hoursWeek:    Int
                   )
