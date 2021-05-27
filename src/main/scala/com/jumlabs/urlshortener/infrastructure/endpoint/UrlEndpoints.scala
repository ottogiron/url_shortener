package com.jumlabs.urlshortener
package infrastructure.endpoint

import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

case class Url(value:String)

class UrlEndpoints[F[_]:Sync] extends Http4sDsl[F]{
  implicit val urlDecoder:EntityDecoder[F, Url] = jsonOf

  private def urlEndpoint():HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root  =>
      Sync[F].delay(Response(Status.Ok))
    }

  private def urlJson(): HttpRoutes[F] =
    HttpRoutes.of[F]{case _ @ GET -> Root / "json1" =>
      Ok(Url("www.jumlabs.com").asJson)
    }

  def endpoints():HttpRoutes[F] =  urlEndpoint() <+> urlJson()
}

object UrlEndpoints {
  def apply[F[_]:Sync]:HttpRoutes[F] = new UrlEndpoints[F].endpoints()
}