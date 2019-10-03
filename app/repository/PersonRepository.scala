package repository

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonRepository @Inject() (databaseConfigProvider: DatabaseConfigProvider )(implicit ec: ExecutionContext){
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private class PeopleTable(tag: Tag) extends Table[models.Person](tag, "person_list") {
    def person_id = column[Long]("person_id", O.PrimaryKey, O.AutoInc)

    def person_name = column[String]("person_name")

    def person_surname = column[String]("person_surname")

    def * = (person_id, person_name, person_surname) <> ((Person.apply _).tupled, Person.unapply)
  }

  private val people = TableQuery[PeopleTable]
  def list(): Future[Seq[Person]] = db.run {
    people.result
  }
  def create(name: String,surname: String): Future[Person] = db.run{
    (people.map(p => (p.person_name,p.person_surname))
      returning people.map(_.person_id)
      into ((nameSurname,id) => Person(id,nameSurname._1,nameSurname._2))
      ) += (name,surname)
  }
  def update(id: Long, nameToChange: String, surNameToChange: String): Future[Int] = db.run{
    people.filter(p => p.person_id === id)
      .map(p => (p.person_id,p.person_name,p.person_surname))
      .update(id,nameToChange,surNameToChange)
  }
  def delete(id: Long): Future[Int] = db.run {
    people.filter(p => p.person_id === id)
      .delete
  }
}
