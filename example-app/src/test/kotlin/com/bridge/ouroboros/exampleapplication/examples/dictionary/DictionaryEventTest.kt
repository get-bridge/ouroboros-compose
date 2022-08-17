package com.bridge.ouroboros.exampleapplication.examples.dictionary

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.receives
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.Test

class DictionaryEventTest {

    @Test
    fun `QueryChanged will change query and trigger effect if not empty`() {
        val newValue = "quite new"
        DictionaryModel() receives DictionaryEvent.QueryChanged(newValue) matches {
            mustHaveModel.query shouldBe newValue
            newEffects shouldHaveSingleElement DictionaryEffect.PerformSearch(newValue)
        }
    }

    @Test
    fun `QueryChange updates with empty string and triggers no effect`() {
        DictionaryModel() receives DictionaryEvent.QueryChanged("") matches {
            mustHaveModel.query shouldBe ""
            shouldNotHaveEffects()
        }
    }

    @Test
    fun `DataLoading will set loading to true`() {
        DictionaryModel(loading = false) receives DictionaryEvent.DataLoading matches {
            mustHaveModel.loading shouldBe true
            shouldNotHaveEffects()
        }
    }

    @Test
    fun `DataLoaded will clear loading flag and set results`() {
        val results = Result.success(emptyList<Definition>())

        DictionaryModel(loading = true, results = null) receives DictionaryEvent.DataLoaded(
            results = results
        ) matches {
            mustHaveModel.loading shouldBe false
            mustHaveModel.results shouldBe results
        }
    }

}