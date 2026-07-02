package com.gassticker

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.json.JSONObject
import java.io.File

class AutomationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val command = intent.getStringExtra("command") ?: return
        val fileName = when (command) {
            "dump_state" -> "automation-state.json"
            "dump_contract" -> "automation-contract.json"
            else -> null
        }

        if (fileName == null) {
            runCatching {
                val args = intent.extras.toJsonObject()
                args.remove("command")
                val result = AutomationBridge.dispatch(
                    AutomationCommand(
                        name = command,
                        args = args,
                    ),
                )
                if (command == "dump_runtime_state") {
                    File(context.filesDir, "automation-runtime-state.json").writeText(result.toString(2))
                    result.put("file", "automation-runtime-state.json")
                }
                setResultCode(if (result.optBoolean("ok", false)) Activity.RESULT_OK else Activity.RESULT_CANCELED)
                setResultData(result.toString())
            }.onFailure { error ->
                setResultCode(Activity.RESULT_CANCELED)
                setResultData(
                    JSONObject()
                        .put("ok", false)
                        .put("command", command)
                        .put("error", error.message ?: error::class.java.simpleName)
                        .toString(),
                )
            }
            return
        }

        runCatching {
            context.assets.open(fileName).use { input ->
                File(context.filesDir, fileName).outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            setResultCode(Activity.RESULT_OK)
            setResultData(
                JSONObject()
                    .put("ok", true)
                    .put("command", command)
                    .put("file", fileName)
                    .toString(),
            )
        }.onFailure { error ->
            setResultCode(Activity.RESULT_CANCELED)
            setResultData(
                JSONObject()
                    .put("ok", false)
                    .put("command", command)
                    .put("error", error.message ?: error::class.java.simpleName)
                    .toString(),
            )
        }
    }
}

private fun Bundle?.toJsonObject(): JSONObject {
    val json = JSONObject()
    this ?: return json
    keySet().forEach { key ->
        val value = get(key)
        when (value) {
            null -> json.put(key, JSONObject.NULL)
            is Boolean -> json.put(key, value)
            is Int -> json.put(key, value)
            is Long -> json.put(key, value)
            is Float -> json.put(key, value.toDouble())
            is Double -> json.put(key, value)
            else -> json.put(key, value.toString())
        }
    }
    return json
}
