package moe.peanutmelonseedbigalmond.disableneteasemusicrecord

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import moe.peanutmelonseedbigalmond.disableneteasemusicrecord.utils.*

class Main:IXposedHookLoadPackage {
    private val packageName= arrayOf("com.netease.cloudmusic")
    lateinit var classLoader: ClassLoader
    override fun handleLoadPackage(p0: XC_LoadPackage.LoadPackageParam) {
        val pn=p0.packageName
        if (pn !in packageName) return
        classLoader=p0.classLoader

        "okhttp3.internal.http.RealInterceptorChain".hookMethod(
            "proceed",
            "okhttp3.Request".findClass(classLoader),
            classLoader=classLoader,
            methodHooker = methodHook {
                beforeMethod{
                    val params=it.args[0]
                    val url=params.callMethod("url").toString()
                    val method=params.callMethod("method").toString()
                    if (url.contains("log")&&!url.contains("login")){
                        it.result= doRejectRequest(params)
                        Utils.Log.i("[Blocked]: $method $url")
                    }else{
                        Utils.Log.i("[Received]: $method $url")
                    }
                }
            }
        )
    }

    private fun doRejectRequest(request: Any): Any {
        val protocol="okhttp3.Protocol".callStaticMethod("get","http/1.1",classLoader=classLoader) // Protocol.HTTP_1_1
        return XposedHelpers.newInstance(
            "okhttp3.Response\$Builder".findClass(classLoader)
        ).callMethod("code",200)
            .callMethod("protocol",protocol)
            .callMethod("message","Request Blocked")
            .callMethod("request",request)
            .callMethod("build")
    }
}