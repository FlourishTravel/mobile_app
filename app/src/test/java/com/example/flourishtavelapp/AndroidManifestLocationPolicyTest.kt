package com.example.flourishtravelapp

import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class AndroidManifestLocationPolicyTest {

  @Test
  fun manifest_declaresForegroundLocationOnly() {
    val manifest = listOf(
      File("src/main/AndroidManifest.xml"),
      File("app/src/main/AndroidManifest.xml")
    ).firstOrNull { it.exists() }
      ?: error("AndroidManifest.xml not found from ${File(".").absolutePath}")

    val xml = manifest.readText()
    assert(xml.contains("ACCESS_FINE_LOCATION"))
    assert(xml.contains("ACCESS_COARSE_LOCATION"))
    assertFalse("Background location must not be declared", xml.contains("ACCESS_BACKGROUND_LOCATION"))
  }
}
