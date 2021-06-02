package org.ottogiron.urlshortener.domain.urls

import org.scalamock.scalatest.MockFactory
import org.scalatest._
import flatspec._
import matchers._
import cats.effect.IO
import org.ottogiron.urlshortener.util.Shortener

import java.time.Instant
import java.time.temporal.ChronoUnit

class UrlServiceSpec extends AnyFlatSpec with MockFactory with should.Matchers {

  "UrlService.shorten" should "shorten an url" in {

    val mockRepository = mock[UrlRepositoryAlgebra[IO]]
    val now = Instant.now()
    val urlVal = "www.google.com"
    val userId:Long = 1
    val hash = Shortener.shorten( s"$userId$urlVal".getBytes())

    val url = Url(
      hash = hash,
      originalUrl = urlVal,
      created = now,
      expirationDate = now.plus(365, ChronoUnit.DAYS),
      userId =  userId
    )

    (mockRepository.create _).expects(url).returning(IO.delay(url))
    val service = new UrlService(mockRepository)
    service.shorten(1, "www.google.com", now)
      .map{ url1 =>
        url1 should be (url)
      }.unsafeRunSync()
  }
}
