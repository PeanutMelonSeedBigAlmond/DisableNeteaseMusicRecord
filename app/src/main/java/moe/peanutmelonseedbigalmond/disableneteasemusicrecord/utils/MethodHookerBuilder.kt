package moe.peanutmelonseedbigalmond.disableneteasemusicrecord.utils

import de.robv.android.xposed.XC_MethodHook

@DslMarker
annotation class MethodHookerDsl

fun methodHook(builder: MethodHooker.() -> Unit): MethodHooker =
    MethodHooker().apply(builder)

@MethodHookerDsl
class MethodHooker {
    var beforeMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {}
    var afterMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {}
    fun beforeMethod(func: (param: XC_MethodHook.MethodHookParam) -> Unit) {
        this.beforeMethod = func
    }

    fun afterMethod(func: (param: XC_MethodHook.MethodHookParam) -> Unit) {
        this.afterMethod = func
    }
}