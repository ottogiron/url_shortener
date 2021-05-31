package org.ottogiron.urlshortener.config

final case class ServerConfig(host: String, port: Int)
final case class UrlShortenerConfig(db: DatabaseConfig, server: ServerConfig)
