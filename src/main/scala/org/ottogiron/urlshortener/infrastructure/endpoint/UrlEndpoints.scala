package org.ottogiron.urlshortener.infrastructure.endpoint

import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
import org.ottogiron.urlshortener.domain.urls.UrlService
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl



class UrlEndpoints[F[_]:Sync](urlService: UrlService[F]) extends Http4sDsl[F]{
  implicit val urlReqDecoder:EntityDecoder[F, UrlRequest] = jsonOf

  private def urlEndpoint():HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root  =>
      Sync[F].pure(Response(Status.Ok))
    }

  private def urlJson(): HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root / "json1" =>
      // Ok(Url("www.jumlabs.com").asJson)
      Ok(urlService.hash(1, "www.google.com"))
    }

  private def shortenUrl(urlService: UrlService[F]): HttpRoutes[F] =
    HttpRoutes.of[F]{
      case req @ POST -> Root / "shorten" =>
        for {
          urlRequest <- req.as[UrlRequest]
          saved <- urlService.shorten(urlRequest.id, urlRequest.url)
          resp <- Ok(saved.asJson)
        } yield resp
    }

  //  private def shorten(): HttpRoutes[F]=
//    HttpRoutes.of[F]{case _ @ POST -> Root / "shorten" =>
//      Ok(Url("www.jumlabs.com"))
//    }

  def endpoints():HttpRoutes[F] =  urlEndpoint() <+> urlJson() <+> shortenUrl(urlService)
}

object UrlEndpoints {
  def endpoints[F[_]:Sync](urlService: UrlService[F]):HttpRoutes[F] =
    new UrlEndpoints[F](urlService).endpoints()
}

