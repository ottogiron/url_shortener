package org.ottogiron.urlshortener

import org.ottogiron.urlshortener.infrastructure.endpoint.UrlRequest
import org.scalacheck._
import org.scalacheck.Arbitrary.arbitrary

trait UrlShortenerArbitraries{

  implicit val urlRequest: Arbitrary[UrlRequest] = Arbitrary[UrlRequest]{
    for {
      url <- arbitrary[String]
      id <- Gen.posNum[Long]
    } yield UrlRequest(id, s"www.$url.com")
  }
}

object UrlShortenerArbitraries extends UrlShortenerArbitraries