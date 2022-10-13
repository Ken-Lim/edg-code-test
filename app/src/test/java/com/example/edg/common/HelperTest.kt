package com.example.edg.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HelperTest {

    @Test
    fun shouldReturnCorrectCurrencyString() {
        // Given double is 12.3
        val value = 12.3

        // When toCurrencyString() is called
        val result = value.toCurrencyString()

        // Then string $12.30 is returned
        assertThat(result).isEqualTo("$12.30")
    }
}
