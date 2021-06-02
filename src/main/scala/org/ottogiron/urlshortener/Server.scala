package org.ottogiron.urlshortener

import cats.effect._
import infrastructure.endpoint.UrlEndpoints
import org.http4s.server.{Router, Server => H4Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import config.{DatabaseConfig, UrlShortenerConfig}
import org.ottogiron.urlshortener.domain.urls.UrlService
// import tsec.passwordhashers.jca.BCrypt
import doobie.util.ExecutionContexts
import io.circe.config.parser
//import tsec.authentication.SecuredRequestHandler
//import tsec.mac.jca.HMACSHA256

import infrastructure.repository.doobie._

object Server extends IOApp {
  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      conf <- Resource.eval(parser.decodePathF[F, UrlShortenerConfig]("url_shortener"))
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      connEc <- ExecutionContexts.fixedThreadPool[F](4)
      txnEc <- ExecutionContexts.cachedThreadPool[F]
      xa <- DatabaseConfig.dbTransactor(conf.db, connEc, Blocker.liftExecutionContext(txnEc))

      //key <- Resource.eval(HMACSHA256.generateKey[F])
      urlRepo = DoobieUrlRepositoryInterpreter(xa)
      urlService = UrlService(urlRepo)
      httpApp = Router(
        "/urls" -> UrlEndpoints.endpoints(urlService)
      ).orNotFound
      server <- BlazeServerBuilder[F](serverEc)
        .bindHttp(8080)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}
