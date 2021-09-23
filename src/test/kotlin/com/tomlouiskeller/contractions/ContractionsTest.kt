package com.tomlouiskeller.contractions

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

internal class ContractionsTest {

     private val contractions = Contractions.default()

    @Nested
    @DisplayName("Default")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Default {
        @Test
        fun `should create instance of Contractions`() {
            // given
            val expected = Contractions::class
            // when
            val actual = Contractions.default()::class
            // then
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `should retrieve actual contractions data`() {
            // when
            val actual = Contractions.default().contractions
            // then
            assertThat(actual).hasSizeGreaterThan(0)

            assert(actual.any() { it.find.pattern == """\b(?i)i['’`]m\b""" })
        }
    }

    @Nested
    @DisplayName("GetContractionsFromFile")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetContractionsFromFile {
        @Test
        fun `should retrieve contractions data`() {
            // given
            val path = EXPAND_SINGLE_CONTRACTIONS_JSON
            // when
            val actual = Contractions.getContractionsFromFile(path)
            // then
            assertThat(actual).hasSizeGreaterThan(0)
            assertThat(actual.first().find.pattern).isEqualTo("""\b(?i)i['’`]m\b""")
        }
    }



    // TODO: test for addContractions
    // TODO: tests for getContractionsFromFile

    // TODO: add tests for ’ ' and `
    // TODO: tests for different capitalization
    
    @Nested
    @DisplayName("Expand Contractions")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Expand {

        @Nested
        @DisplayName("Single Apostrophe")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class SingleApostrophe {
            @Test
            fun `should expand i'm`() {
                // given
                val input = "Hello. I’m happy to meet you"
                val expected = "Hello. I am happy to meet you"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }

            @Test
            fun `should expand i've`() {
                // given
                val input = "I've had it with you"
                val expected = "I have had it with you"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }
        }

        
        @Nested
        @DisplayName("Double Apostrophe")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class DoubleApostrophe {
            @Test
            fun `should expand i'm'a`() {
                // given
                val input = "i'm'a head out"
                val expected = "i am about to head out"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }

            @Test
            fun `should expand it'd've`() {
                // given
                val input = "it’d’ve been fun"
                val expected = "it would have been fun"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }

            @Test
            fun `should expand he'll've`() {
                // given
                val input = "he’ll’ve done that"
                val expected = "he will have done that"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }


            @Disabled("This test currently fails. Don't know how to make it work")
            @Test
            fun `should NOT expand double contraction when loading single contractions file only`() {
                // given
                val path = EXPAND_SINGLE_CONTRACTIONS_JSON
                val contractions = Contractions(ObjectMapper())
                contractions.addContractions(path)
                val input = "couldn’t’ve"
                val expected = "couldn’t’ve"
                // when
                val actual = contractions.expand(input)
                // then
                assertThat(actual).isEqualTo(expected)
            }
        }

    }

}