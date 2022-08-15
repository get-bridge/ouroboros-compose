package com.bridge.ouroboros.compose

import androidx.compose.runtime.DisallowComposableCalls
import kotlinx.coroutines.CoroutineScope

typealias EventConsumer<EVENT> = (EVENT) -> Unit

typealias CrashHandler = (Throwable) -> Unit

typealias LoopInitializer<MODEL, EFFECT> = @DisallowComposableCalls () -> Next.Change<MODEL, EFFECT>

typealias EffectStateFactory<EFFECT_STATE> = @DisallowComposableCalls () -> (EFFECT_STATE)

typealias CoroutineScopeCustomizer = (CoroutineScope) -> CoroutineScope
