package com.jumlabs.urlshortener.domain
package urls

trait UrlRepositoryAlgebra[F[_]] {
  def create(url:Url):F[Url]
  def get(hash:String):F[Url]
  def delete(hash:String):F[Url]
}
