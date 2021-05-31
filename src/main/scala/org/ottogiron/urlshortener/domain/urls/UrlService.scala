package org.ottogiron.urlshortener.domain.urls

import org.ottogiron.urlshortener.util.Shortener

//import cats.Functor
//import cats.data.EitherT
//import cats.syntax.all._
import java.time.temporal.ChronoUnit

import java.time.Instant

class UrlService[F[_]](urlRepository:UrlRepositoryAlgebra[F]) {

  def hash(userId: Int, url: String): String = {
    Shortener.shorten((userId + url).getBytes())
  }

  def shorten(userId:Int, url:String, now:Instant = Instant.now()): F[Url] = {
    val shortened:String = hash(userId, url)    
    urlRepository.create(Url(
      hash = shortened,
      originalUrl =  url,
      now,
      expirationDate = now.plus(365, ChronoUnit.DAYS),
      userId = userId
    ))
  }

}
