package employee

import java.sql.Timestamp

case class Employee(
                     id: Option[Long],
                     firstName: String,
                     lastName: String,
                     email: String,
                     mobile: Option[String],
                     address: Option[String],
                     createdAt: Timestamp,
                     updatedAt: Timestamp
                   )
