package moe.peanutmelonseedbigalmond.disableneteasemusicrecord.utils

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

/***** Class<*>.XXX *****/
fun Class<*>.hookMethod(methodName: String?, vararg params: Any?, methodHooker: MethodHooker) =
    try {
        params.forEachIndexed { index, param ->
            if (param !is String && param !is Class<*>) {
                Utils.Log.e("[$param type is wrong, type is ${param?.javaClass}], index: $index")
            }
        }
        XposedHelpers.findAndHookMethod(this, methodName, *params, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                methodHooker.beforeMethod.invoke(param)
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                methodHooker.afterMethod.invoke(param)
            }
        })

        Utils.Log.i("Hook ${this.name}.$methodName Successful")
    } catch (e: Throwable) {
        when (e) {
            is NoSuchMethodError,
            is XposedHelpers.ClassNotFoundError,
            is ClassNotFoundException -> {
                Utils.Log.e(e.localizedMessage)
                XposedBridge.log(e)
            }
            else -> throw e
        }
    }

fun Class<*>.hookAllConstructors(
    methodHooker: MethodHooker
)=try{
    XposedBridge.hookAllConstructors(this,object :XC_MethodHook(){
        override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
            methodHooker.beforeMethod
        }

        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            methodHooker.afterMethod
        }
    })
    Utils.Log.i("Hook ${this.name} constructors Successful")
}catch (e:Throwable){
    when (e) {
        is NoSuchMethodError,
        is XposedHelpers.ClassNotFoundError,
        is ClassNotFoundException -> {
            Utils.Log.e(e.localizedMessage)
            XposedBridge.log(e)
        }
        else -> throw e
    }
}

fun Class<*>.callStaticMethod(methodName: String?, vararg args: Any?): Any? =
    XposedHelpers.callStaticMethod(this, methodName, *args)

/***** Member.XXX *****/

/***** String.XXX *****/
fun String.findClass(classLoader: ClassLoader): Class<*> =
    XposedHelpers.findClass(this, classLoader)

fun String.hookAfterMethod(
    methodName: String?,
    vararg params: Any?,
    classLoader: ClassLoader,
    afterMethodFunc: (param: XC_MethodHook.MethodHookParam) -> Unit
) = try {
    findClass(classLoader).hookMethod(
        methodName,
        *params,
        methodHooker = methodHook {
            afterMethod = afterMethodFunc
        }
    )
} catch (th: Throwable) {
    when (th) {
        is XposedHelpers.ClassNotFoundError,
        is ClassNotFoundException -> Utils.Log.e(th.localizedMessage)
        else -> throw th
    }
}

fun String.hookBeforeMethod(
    methodName: String?,
    vararg params: Any?,
    classLoader: ClassLoader,
    beforeMethodFunc: (param: XC_MethodHook.MethodHookParam) -> Unit
) = try {
    findClass(classLoader).hookMethod(
        methodName,
        *params,
        methodHooker = methodHook {
            beforeMethod = beforeMethodFunc
        }
    )
} catch (th: Throwable) {
    when (th) {
        is XposedHelpers.ClassNotFoundError,
        is ClassNotFoundException -> Utils.Log.e(th.localizedMessage)
        else -> throw th
    }
}

fun String.hookMethod(
    methodName: String?,
    vararg params: Any?,
    classLoader: ClassLoader,
    methodHooker: MethodHooker
) = try {
    findClass(classLoader).hookMethod(
        methodName,
        *params,
        methodHooker = methodHooker
    )
} catch (th: Throwable) {
    when (th) {
        is XposedHelpers.ClassNotFoundError,
        is ClassNotFoundException -> Utils.Log.e(th.localizedMessage)
        else -> throw th
    }
}

fun String.hookAllConstructors(
    classLoader: ClassLoader,
    methodHooker: MethodHooker
)=try{
    findClass(classLoader).hookAllConstructors(methodHooker)
}catch (th:Throwable){
    when (th) {
        is XposedHelpers.ClassNotFoundError,
        is ClassNotFoundException -> Utils.Log.e(th.localizedMessage)
        else -> throw th
    }
}

fun String.callStaticMethod(
    methodName: String?,
    vararg args: Any?,
    classLoader: ClassLoader
) = findClass(classLoader).callStaticMethod(methodName, *args)

/***** Any.XXX *****/
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldAs(fieldName: String): T {
    return XposedHelpers.getObjectField(this, fieldName) as T
}

fun Any.callMethod(methodName: String?, vararg args: Any?): Any{
    return XposedHelpers.callMethod(this, methodName, *args)
}


