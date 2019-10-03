package repository

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonRepository @Inject()(databaseConfigProvider: DatabaseConfigProvider)(
  implicit ec: ExecutionContext
) {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private class PeopleTable(tag: Tag)
      extends Table[models.Person](tag, "person_list") {
    def personId = column[Long]("person_id", O.PrimaryKey, O.AutoInc)

    def personName = column[String]("person_name")

    def personSurName = column[String]("person_surname")

    def * =
      (personId, personName, personSurName) <> ((Person.apply _).tupled, Person.unapply)
  }

  private val people = TableQuery[PeopleTable]
  def list(): Future[Seq[Person]] = db.run {
    people.result
  }
  def create(name: String, surname: String): Future[Person] = db.run {
    (people.map(p => (p.personName, p.personSurName))
      returning people.map(_.personId)
      into ((nameSurname, id) => Person(id, nameSurname._1, nameSurname._2))) += (name, surname)
  }
  def update(id: Long,
             nameToChange: String,
             surNameToChange: String): Future[Int] = db.run {
    people
      .filter(p => p.personId === id)
      .map(p => (p.personId, p.personName, p.personSurName))
      .update(id, nameToChange, surNameToChange)
  }
  def delete(id: Long): Future[Int] = db.run {
    people.filter(p => p.personId === id).delete
  }
}
