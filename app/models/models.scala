package models
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms._

case class Person(person_id: Long, person_name: String, person_surname: String)
case class CreatePersonForm(person_name: String, person_surname: String)

// case class UserForm(userName: String, password: String)

object Person {
  val personForm: Form[CreatePersonForm] = Form {
    mapping("person_name" -> nonEmptyText, "person_surname" -> nonEmptyText)(
      CreatePersonForm.apply
    )(CreatePersonForm.unapply)
  }
  implicit val personFormat = Json.format[Person]
}
