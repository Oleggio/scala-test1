package service

import java.time.LocalTime

import scala.concurrent.ExecutionContextExecutor
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.StatusCodes
import org.json4s._
import org.json4s.jackson.Serialization.write
import repository.Repository

object WebService extends App {
  def start(): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    implicit val formats = DefaultFormats

    val routes =
      pathPrefix("rooms") {
        // Post a room booking if there is the time slot available
        // N.B! Returns 200 OK response code if there is no time slot with corresponding message.
        post {
          parameters('roomId.as[Long], "bookingStart", "bookingEnd") {
            (roomId, bookingStart, bookingEnd) =>
              val booking = domain.RoomBooking(
                roomId,
                java.sql.Time.valueOf(LocalTime.parse(bookingStart)),
                java.sql.Time.valueOf(LocalTime.parse(bookingEnd)))
              complete((StatusCodes.Accepted, repository.Repository.save(booking)))
          }
        } ~
          get {
            parameters("bookingStart", "bookingEnd") {
              (bookingStart, bookingEnd) =>
                var roomList = repository.Repository.lookUpAll(
                  java.sql.Time.valueOf(LocalTime.parse(bookingStart)),
                  java.sql.Time.valueOf(LocalTime.parse(bookingEnd)))
                complete((StatusCodes.Accepted, write(roomList.toList.sorted)))
            }
          }
      }

    val bindingFuture =
      Http().bindAndHandle(routes, "0.0.0.0", 8080).recoverWith {
        case _ => sys.exit(1)
      }

    Repository.InitiateRepository()

    sys.addShutdownHook {
      bindingFuture.map(_.unbind())
    }
  }

  start()
}
