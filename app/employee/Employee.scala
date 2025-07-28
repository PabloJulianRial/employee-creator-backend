package employee

import java.sql.{Date, Timestamp}

case class Employee(
                     id: Option[Long] = None,
                     firstName: String,
                     lastName: String,
                     email: String,
                     mobileNumber: Option[String],
                     address: Option[String],
                     contractStart: Date,
                     contractType: String,
                     contractTime: String,
                     contractEnd: Option[Date],
                     hoursPerWeek: Int,
                     createdAt: Timestamp,
                     updatedAt: Timestamp
                   )
