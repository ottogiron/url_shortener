package org.ottogiron.urlshortener
package infrastructure.endpoint

import domain.urls._
import cats.effect._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.server.Router
import org.ottogiron.urlshortener.domain.urls.{UrlRepositoryAlgebra, UrlService}
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.matchers._

class UrlEndpointsSpec
  extends AnyFunSuite
    with should.Matchers
    with MockFactory
    with ScalaCheckPropertyChecks
    with UrlShortenerArbitraries
    with Http4sDsl[IO]
    with Http4sClientDsl[IO] {
  implicit val urlReqEncoder: Encoder[UrlRequest] = deriveEncoder
  implicit val urlReqEnc: EntityEncoder[IO, UrlRequest] = jsonEncoderOf

  implicit val urlDecoder: Decoder[Url] = deriveDecoder
  implicit val urlDec: EntityDecoder[IO, Url] = jsonOf

  val urlRepo = new UrlRepositoryAlgebra[IO] {
    override def create(url: Url): IO[Url] = IO.pure(url)

    override def get(hash: String): IO[Option[Url]] = ???

    override def delete(hash: String): IO[Option[Url]] = ???
  }

  val urlService = UrlService(urlRepo)

  def getTestResources: HttpApp[IO] = {

    val urlEndpoint = UrlEndpoints.endpoints[IO](urlService)
    val urlRoutes = Router(("/urls", urlEndpoint)).orNotFound
    urlRoutes
  }

  test("shorten a url") {
    val urlRoutes = getTestResources

    forAll { ( urlRequest: UrlRequest) =>
      val hash = urlService.shorten(urlRequest.id, urlRequest.url).unsafeRunSync().hash
      (for {
        shortenRequest <- POST(urlRequest, uri"/urls/shorten")
        shortenResp <- urlRoutes.run(shortenRequest)
        shortenedResp <- shortenResp.as[Url]
      } yield {
        shortenResp.status shouldEqual Ok
        shortenedResp.hash shouldEqual hash
      }
        ).unsafeRunSync()
    }

  }

}
