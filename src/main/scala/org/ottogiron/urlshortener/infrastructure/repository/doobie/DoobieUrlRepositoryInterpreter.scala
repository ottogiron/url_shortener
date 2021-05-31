package org.ottogiron.urlshortener
package infrastructure.repository.doobie

import cats.data.OptionT
import cats.effect.Bracket
import cats.syntax.all._
import doobie._
import doobie.implicits._
import doobie.implicits.legacy.instant._
import domain.urls.{Url, UrlRepositoryAlgebra}

private object UrlSQL {
  def select(hash:String):Query0[Url] =
    sql"""
          SELECT * FROM urls
          WHERE hash = $hash
      """.query[Url]

  def insert(url: Url): Update0 =
    sql"""
        INSERT INTO urls (hash, original_url,created, expiration_date, user_id)
        VALUES (${url.hash}, ${url.originalUrl}, ${url.created}, ${url.expirationDate}, ${url.userId})
      """
      .update

  def delete(hash:String): Update0 =
    sql"""
        DELETE FROM urls
        WHERE hash = $hash
      """.update
}

class DoobieUrlRepositoryInterpreter[F[_]: Bracket[*[_], Throwable]](val xa:Transactor[F])
 extends UrlRepositoryAlgebra[F]
{

  import UrlSQL._

  override def create(url: Url): F[Url] =
    insert(url)
      .run
      .map(_ => url.copy())
      .transact(xa)


  override def get(hash: String): F[Option[Url]] =
    select(hash).option.transact(xa)

  override def delete(hash: String): F[Option[Url]] =
    OptionT(get(hash))
      .semiflatTap(url => UrlSQL.delete(hash).run.transact(xa).as(url))
      .value
}

