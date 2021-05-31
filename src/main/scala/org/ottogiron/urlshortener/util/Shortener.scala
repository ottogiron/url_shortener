package org.ottogiron.urlshortener.util

import org.ottogiron.urlshortener.security.Digest

object Shortener {
  def shorten(input:Array[Byte]) =
    Encoding.base64(Digest.md5(input)).substring(0,7)
}
