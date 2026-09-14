package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Grand Hospitality", appName)
  }

  @Test
  fun `verify grade calculator calculation`() {
    val result = com.example.data.util.GradeCalculator.calculate(
      kehadiran = 95.0,
      tugas = 90.0,
      praktik = 92.0,
      uts = 88.0,
      uas = 90.0
    )
    assertEquals("A", result.nilaiHuruf)
    assertEquals(4.0, result.bobot, 0.01)
    assertEquals("Kompeten", result.statusKelulusan)
  }
}
