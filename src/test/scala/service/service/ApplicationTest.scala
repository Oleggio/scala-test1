package service.service

import org.scalatest.{Matchers, WordSpec}
import com.softwaremill.sttp._
import service.WebService

class ApplicationTest extends WordSpec with Matchers{
  "Service" should {
    "response on target port" in {
      implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
      WebService.start()
      sttp.get(uri"http://localhost:8080").send().code shouldBe 200
    }
  }
}
