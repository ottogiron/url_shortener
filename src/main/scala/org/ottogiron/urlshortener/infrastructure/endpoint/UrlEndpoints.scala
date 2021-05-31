package org.ottogiron.urlshortener.infrastructure.endpoint

import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
//import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.ottogiron.urlshortener.domain.urls.Url



class UrlEndpoints[F[_]:Sync] extends Http4sDsl[F]{
  implicit val urlDecoder:EntityDecoder[F, Url] = jsonOf

  private def urlEndpoint():HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root  =>
      Sync[F].delay(Response(Status.Ok))
    }

  private def urlJson(): HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root / "json1" =>
      // Ok(Url("www.jumlabs.com").asJson)
      Ok("")
    }

//  private def shorten(): HttpRoutes[F]=
//    HttpRoutes.of[F]{case _ @ POST -> Root / "shorten" =>
//      Ok(Url("www.jumlabs.com"))
//    }

  def endpoints():HttpRoutes[F] =  urlEndpoint() <+> urlJson()
}

object UrlEndpoints {
  def apply[F[_]:Sync]:HttpRoutes[F] = new UrlEndpoints[F].endpoints()
}