package org.ottogiron.urlshortener.domain.urls

import java.time.Instant

case class Url(hash: String, originalUrl: String, created: Instant, expirationDate: Instant, userId: Int)
