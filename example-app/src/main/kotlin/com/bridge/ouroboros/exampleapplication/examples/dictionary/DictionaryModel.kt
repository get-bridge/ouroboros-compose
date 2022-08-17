package com.bridge.ouroboros.exampleapplication.examples.dictionary

import com.bridge.ouroboros.compose.ActionableEvent
import com.bridge.ouroboros.compose.EventConsumer
import com.bridge.ouroboros.compose.ExecutableEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DictionaryModel(
    val loading: Boolean = false,
    val query: String = "",
    val results: Result<List<Definition>>? = null
)

sealed class DictionaryEvent : ActionableEvent<DictionaryModel, DictionaryEffect> {

    data class QueryChanged(val query: String) : DictionaryEvent() {
        override fun perform(model: DictionaryModel) = change(
            model.copy(query = query),
            effects = if (query.isNotBlank())
                arrayOf(DictionaryEffect.PerformSearch(query))
            else
                emptyArray()
        )
    }

    object DataLoading : DictionaryEvent() {
        override fun perform(model: DictionaryModel) = change(
            model.copy(loading = true)
        )
    }

    data class DataLoaded(val results: Result<List<Definition>>) : DictionaryEvent() {
        override fun perform(model: DictionaryModel) = change(
            model.copy(loading = false, results = results)
        )
    }

}

sealed class DictionaryEffect : ExecutableEffect<DictionaryEvent, DictionaryEffect.State>() {

    data class PerformSearch(val query: String) : DictionaryEffect() {
        override fun State.perform(emit: EventConsumer<DictionaryEvent>) {
            searchJob?.cancel()
            searchJob = launch {
                // very simple debouncing with cancel and delay
                delay(1000)
                emit(DictionaryEvent.DataLoading)
                emit(DictionaryEvent.DataLoaded(service.lookup(query)))
            }
        }
    }

    class State(
        val service: DictionaryService = KtorDictionaryService(),
        var searchJob: Job? = null
    )

}