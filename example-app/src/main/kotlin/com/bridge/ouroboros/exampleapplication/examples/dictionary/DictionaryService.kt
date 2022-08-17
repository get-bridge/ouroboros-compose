package com.bridge.ouroboros.exampleapplication.examples.dictionary

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

interface DictionaryService {
    suspend fun lookup(word: String): Result<List<Definition>>
}

class KtorDictionaryService(
    private val client: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
) : DictionaryService {
    override suspend fun lookup(word: String): Result<List<Definition>> =
        client.get("https://api.dictionaryapi.dev/api/v2/entries/en/") {
            url {
                appendPathSegments(word)
            }
        }.unwrapAsDefinitions()
}

suspend fun HttpResponse.unwrapAsDefinitions(): Result<List<Definition>> = try {
    when {
        status.isSuccess() -> Result.success(body())
        status == HttpStatusCode.NotFound -> Result.success(emptyList())
        else -> Result.failure(IOException("Failed with status $status"))
    }
} catch (e: Throwable) {
    Result.failure(e)
}

@Serializable
data class Definition(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<Phonetic> = emptyList(),
    val origin: String? = null,
    val meanings: List<Meaning> = emptyList()
)

@Serializable
data class Phonetic(
    val text: String,
    val audio: String? = null
)

@Serializable
data class Meaning(
    val partOfSpeech: PartOfSpeech,
    val definitions: List<SubDefinition> = emptyList()
)

@Serializable
enum class PartOfSpeech {
    @SerialName("adjective")
    ADJECTIVE,

    @SerialName("exclamation")
    EXCLAMATION,

    @SerialName("noun")
    NOUN,

    @SerialName("verb")
    VERB
}

@Serializable
data class SubDefinition(
    val definition: String,
    val example: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)