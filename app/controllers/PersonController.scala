package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._
import play.api.libs.json.Json
import repository.PersonRepository
import services.PersonService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonController @Inject()(cc: ControllerComponents,personService: PersonService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    personService.list().map{
      people => Ok(Json.toJson(people))
    }
  }
  /*
  def deneme(): Action[Person] = Action(parse.json[Person]) { request  =>
    println(request.body.person_name)
    Ok(Json.toJson(request.body))
  }
   */

  def addPerson() = Action.async { implicit request: Request[AnyContent] =>
    Person.personForm.bindFromRequest.fold(
      errorForm => Future.successful(BadRequest),
      person => personService.create(person.person_name,person.person_surname).map(
        _ => Ok
      )
    )
  }
  def updatePerson(personId: Long) = Action.async { implicit request: Request[AnyContent] =>
    Person.personForm.bindFromRequest.fold(
      errorForm => Future.successful(BadRequest),
      person => personService.update(personId,person.person_name,person.person_surname).map(
        _ => Ok
      )
    )
  }
  def 


}
