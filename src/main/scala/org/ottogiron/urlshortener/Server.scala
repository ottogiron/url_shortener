package org.ottogiron.urlshortener

import cats.effect._
import infrastructure.endpoint.UrlEndpoints
import org.http4s.server.{Router, Server => H4Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
// import tsec.passwordhashers.jca.BCrypt
import doobie.util.ExecutionContexts
// import io.circe.config.parser
// import tsec.authentication.SecuredRequestHandler
// import tsec.mac.jca.HMACSHA256

object Server extends IOApp {
  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      // connEc <- ExecutionContexts.fixedThreadPool[F](4)
//      txnEc <- ExecutionContexts.cachedThreadPool[F]
//      key <- Resource.eval(HMACSHA256.generateKey[F])
      httpApp = Router(
        "/urls" -> UrlEndpoints[F]
      ).orNotFound
      server <- BlazeServerBuilder[F](serverEc)
        .bindHttp(8080)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}
