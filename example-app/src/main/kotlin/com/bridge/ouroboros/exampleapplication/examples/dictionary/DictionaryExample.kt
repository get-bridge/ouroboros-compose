@file:OptIn(ExperimentalFoundationApi::class)

package com.bridge.ouroboros.exampleapplication.examples.dictionary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bridge.ouroboros.compose.EventConsumer
import com.bridge.ouroboros.compose.Next
import com.bridge.ouroboros.compose.acquireLoop
import com.bridge.ouroboros.exampleapplication.R
import com.bridge.ouroboros.exampleapplication.theme.AppTheme

@Composable
fun DictionaryExample() {
    val loop = acquireLoop(
        loopInitializer = { Next.Change<DictionaryModel, DictionaryEffect>(DictionaryModel()) },
        effectStateFactory = DictionaryEffect::State
    )

    DictionaryScreen(model = loop.model, dispatchEvent = loop::dispatchEvent)
}

@Composable
fun DictionaryScreen(model: DictionaryModel, dispatchEvent: EventConsumer<DictionaryEvent>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = model.query,
                onValueChange = { dispatchEvent(DictionaryEvent.QueryChanged(it)) },
                label = { Text(stringResource(id = R.string.search)) },
                placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
                singleLine = true
            )
        }

        if (model.loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        if (model.results != null) {
            model.results.onSuccess { definitions ->
                if (definitions.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.no_results)
                        )
                    }
                } else {
                    items(definitions) { definition ->
                        Definition(
                            modifier = Modifier.animateItemPlacement(),
                            definition = definition
                        )
                    }
                }
            }

            model.results.onFailure {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.request_error),
                    )
                }
            }
        }
    }
}

@Composable
private fun Definition(
    modifier: Modifier = Modifier,
    definition: Definition
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = definition.word,
                style = MaterialTheme.typography.h5
            )

            definition.phonetic?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.subtitle1
                )
            }

            definition.origin?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1
                )
            }

            definition.meanings.forEach { meaning ->
                Divider()

                Text(
                    text = meaning.partOfSpeech.textResource,
                    style = MaterialTheme.typography.overline
                )

                meaning.definitions.forEach {
                    Text(text = it.definition)
                }
            }
        }
    }
}

@Composable
@Preview
fun DefinitionPreview() {
    AppTheme {
        Definition(definition = Definition(
            word = "rose",
            phonetic = "/ɹəʊz/",
            origin = "The name rose comes from Latin rosa, which was perhaps borrowed from Oscan, from Greek ῥόδον rhódon (Aeolic βρόδον wródon), itself borrowed from Old Persian wrd- (wurdi), related to Avestan varəδa, Sogdian ward, Parthian wâr.",
            meanings = listOf(
                Meaning(
                    partOfSpeech = PartOfSpeech.NOUN,
                    definitions = listOf(
                        SubDefinition(
                            definition = "A shrub of the genus Rosa, with red, pink, white or yellow flowers."
                        ),
                        SubDefinition(
                            definition = "A purplish-red or pink colour, the colour of some rose flowers.",
                        )
                    )
                ),
                Meaning(
                    partOfSpeech = PartOfSpeech.VERB,
                    definitions = listOf(
                        SubDefinition(
                            definition = "To make rose-coloured; to redden or flush."
                        ),
                        SubDefinition(
                            definition = "To perfume, as with roses.",
                        )
                    )
                ),
                Meaning(
                    partOfSpeech = PartOfSpeech.ADJECTIVE,
                    definitions = listOf(
                        SubDefinition(
                            definition = "Having a purplish-red or pink colour. See rosy."
                        )
                    )
                )
            )
        ))
    }
}

val PartOfSpeech.textResource: String @Composable get() = when (this) {
    PartOfSpeech.ADJECTIVE -> stringResource(id = R.string.adjective)
    PartOfSpeech.EXCLAMATION -> stringResource(id = R.string.exclamation)
    PartOfSpeech.NOUN -> stringResource(id = R.string.noun)
    PartOfSpeech.VERB -> stringResource(id = R.string.verb)
}
