package org.ottogiron.urlshortener
package domain.urls

trait UrlRepositoryAlgebra[F[_]] {
  def create(url:Url):F[Url]
  def get(hash:String):F[Option[Url]]
  def delete(hash:String):F[Option[Url]]
}
