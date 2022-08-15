package com.bridge.ouroboros.compose

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class LoopState<MODEL : Any, EVENT : ActionableEvent<MODEL, EFFECT>, EFFECT : ExecutableEffect<EVENT, EFFECT_STATE>, EFFECT_STATE>(
    initialModel: MODEL,
    initialEffects: Set<EFFECT>,
    private val effectState: EFFECT_STATE,
    loopScope: CoroutineScope,
    private val crashHandler: CrashHandler,
    private val debugLogger: ((String) -> Unit)?
) {
    private val effectsContext =
        loopScopeCustomizer(loopScope) + CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                crashHandler(throwable)
            }
        }

    var model by mutableStateOf<MODEL>(initialModel.also {
        debugLogger?.invoke("Loop initialized with model: $it")
    })
        private set

    init {
        debugLogger?.invoke("Running initial effects: $initialEffects")
        runEffects(initialEffects)
    }

    fun dispatchEvent(event: EVENT) {
        val next = event.perform(model)

        next.ifHasModel { newModel ->
            model = newModel
            debugLogger?.invoke("Received new model: $model")
        }

        next.ifHasEffects { effects ->
            debugLogger?.invoke("Running effects: $effects")
            runEffects(effects)
        }
    }

    private fun runEffects(effects: Set<EFFECT>) {
        for (effect in effects) {
            try {
                effect.runInContext(
                    effectsContext.coroutineContext,
                    effectState,
                    ::dispatchEvent
                )
            } catch (e: Throwable) {
                // Just in case, effects do not necessarily launch coroutines
                crashHandler(e)
            }
        }
    }

    companion object {

        @Volatile
        var loopScopeCustomizer: CoroutineScopeCustomizer = { it }

    }
}

class LoopStateViewModel<MODEL : Any, EVENT : ActionableEvent<MODEL, EFFECT>, EFFECT : ExecutableEffect<EVENT, EFFECT_STATE>, EFFECT_STATE>(
    initialModel: MODEL,
    initialEffects: Set<EFFECT>,
    effectState: EFFECT_STATE,
    crashHandler: CrashHandler = {
        Log.e(
            "OuroborosEffects",
            "Error occured in Ouroboros Effect",
            it
        )
    },
    debugLogger: ((String) -> Unit)? = { Log.d("Ouroboros", it) },
    externalEvents: Flow<EVENT>? = null
) : ViewModel() {
    val loop = LoopState(
        initialModel,
        initialEffects,
        effectState,
        viewModelScope,
        crashHandler,
        debugLogger
    )

    init {
        if (externalEvents != null) {
            viewModelScope.launch {
                externalEvents.collect(loop::dispatchEvent)
            }
        }
    }
}

@Composable
inline fun <MODEL : Any, EVENT : ActionableEvent<MODEL, EFFECT>, EFFECT : ExecutableEffect<EVENT, EFFECT_STATE>, EFFECT_STATE> acquireLoop(
    crossinline loopInitializer: LoopInitializer<MODEL, EFFECT>,
    crossinline effectStateFactory: EffectStateFactory<EFFECT_STATE>,
    noinline crashHandler: CrashHandler = {
        Log.e(
            "OuroborosEffects",
            "Error occured in Ouroboros Effect",
            it
        )
    },
    noinline debugLogger: ((String) -> Unit)? = { Log.d("Ouroboros", it) },
    externalEvents: Flow<EVENT>? = null,
): LoopState<MODEL, EVENT, EFFECT, EFFECT_STATE> {
    val viewModel =
        viewModel<LoopStateViewModel<MODEL, EVENT, EFFECT, EFFECT_STATE>>(factory = remember {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val (model, effects) = loopInitializer()
                    val effectState = effectStateFactory()

                    @Suppress("UNCHECKED_CAST")
                    return LoopStateViewModel(
                        initialModel = model,
                        initialEffects = effects,
                        effectState = effectState,
                        crashHandler = crashHandler,
                        debugLogger = debugLogger,
                        externalEvents = externalEvents
                    ) as T
                }
            }
        })

    return viewModel.loop
}
