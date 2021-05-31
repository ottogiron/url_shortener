package org.ottogiron.urlshortener.util

import java.util.Base64

object Encoding {
  def base64(input:Array[Byte]) =
    Base64.getEncoder.encodeToString(input)
}
