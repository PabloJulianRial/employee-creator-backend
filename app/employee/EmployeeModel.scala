package models.employee

case class Employee(
                     id: Option[Long] = None,
                     firstName: String,
                     lastName:  String,
                     email:     String,
                     mobile:    String,
                     address:   String
                   )
