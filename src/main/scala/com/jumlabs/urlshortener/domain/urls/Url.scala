package com.jumlabs.urlshortener.domain
package urls

import java.time.Instant

case class Url(hash: String, originalUrl: String, created: Instant, expirationDate: Instant, userId: Int)
