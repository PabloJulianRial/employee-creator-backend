package models.contract

import java.sql.Timestamp

case class Contract(
                     id:           Option[Long] = None,
                     employeeId:   Long,
                     startDate:    Timestamp,
                     endDate:      Option[Timestamp],
                     contractType: String,
                     contractTime: Boolean,
                     hoursWeek:    Int
                   )
