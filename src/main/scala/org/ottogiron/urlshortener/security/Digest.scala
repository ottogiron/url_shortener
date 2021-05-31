package org.ottogiron.urlshortener.security

import java.security.MessageDigest

object Digest {
  def md5(input:Array[Byte]):Array[Byte] =
    MessageDigest.getInstance("MD5")
      .digest(input)
}
