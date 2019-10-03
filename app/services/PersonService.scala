package services

import javax.inject.{Inject, Singleton}
import models.Person
import repository.PersonRepository

import scala.concurrent.Future
@Singleton
class PersonService @Inject()(personRepository: PersonRepository) {
  def create(name: String, surName: String): Future[Person] ={
    personRepository.create(name,surName)
  }
  def list(): Future[Seq[Person]] = {
    personRepository.list()
  }
  def update(id: Long, name: String,surName: String): Future[Int] = {
    personRepository.update(id,name,surName)
  }
  def delete(id: Long): Future[Int] =
    personRepository.delete(id)
}
