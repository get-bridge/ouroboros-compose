package com.bridge.ouroboros.exampleapplication.examples.dictionary

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.runWith
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Job
import org.junit.Test

class DictionaryEffectTest {

    // MockK does not quite support inline classes as return parameters right now
    class MockDictionaryService : DictionaryService {
        var lookupWasCalled = false
        var lookupResult = Result.success<List<Definition>>(emptyList())

        override suspend fun lookup(word: String): Result<List<Definition>> {
            lookupWasCalled = true
            return lookupResult
        }
    }

    @Test
    fun `PerformSearch will contact the service and clear previous job`() {
        val service = MockDictionaryService()
        val job = mockk<Job>(relaxed = true)

        DictionaryEffect.PerformSearch("word") runWith DictionaryEffect.State(service, job) matches {
            expectEvents(
                DictionaryEvent.DataLoading,
                DictionaryEvent.DataLoaded(service.lookupResult)
            )
        }

        service.lookupWasCalled shouldBe true

        coVerify {
            job.cancel()
        }

        confirmVerified(job)
    }

}