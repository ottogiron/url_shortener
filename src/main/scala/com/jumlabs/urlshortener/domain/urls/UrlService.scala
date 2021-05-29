package com.jumlabs.urlshortener.domain
package urls

import java.security.MessageDigest
import cats.Functor
import cats.data.EitherT
import cats.syntax.all._
import java.time.temporal.ChronoUnit

import java.time.Instant
import java.util.Base64

class UrlService[F[_]](urlRepository:UrlRepositoryAlgebra[F]) {

  def hash(userId: Int, url: String): String = {
    val digest = MessageDigest.getInstance("MD5")
      .digest((url + userId).getBytes())
    Base64.getEncoder.encodeToString(digest)
  }

  def shorten(userId:Int, url:String): F[Url] = {
    val shortened:String = hash(userId, url)    
    urlRepository.create(Url(
      hash = shortened,
      originalUrl =  url,
      Instant.now(),
      expirationDate = Instant.now().plus(12, ChronoUnit.MONTHS),
      userId = userId
    ))
  }

}
