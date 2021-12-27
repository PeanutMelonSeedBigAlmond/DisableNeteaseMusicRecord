package moe.peanutmelonseedbigalmond.disableneteasemusicrecord.utils

import android.content.Context
import android.widget.Toast
import de.robv.android.xposed.XposedBridge

object Utils {
    object Log {
        fun i(msg: String) {
            XposedBridge.log("[DisableNeteaseMusicRecord] [INFO]: $msg")
        }

        fun e(msg: String){
            XposedBridge.log("[DisableNeteaseMusicRecord] [ERROR]: $msg")
        }
    }

    object Tip {
        fun show(context: Context, msg: String) {
            Toast.makeText(context, "[DisableNeteaseMusicRecord]: $msg", Toast.LENGTH_SHORT).show()
        }
    }
}