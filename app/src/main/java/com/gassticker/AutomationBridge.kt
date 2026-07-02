package com.gassticker

import org.json.JSONObject

data class AutomationCommand(
    val name: String,
    val args: JSONObject,
)

object AutomationBridge {
    @Volatile
    var rootHandler: ((AutomationCommand) -> JSONObject)? = null

    @Volatile
    var editorHandler: ((AutomationCommand) -> JSONObject)? = null

    @Volatile
    var rootStateProvider: (() -> JSONObject)? = null

    @Volatile
    var editorStateProvider: (() -> JSONObject)? = null

    fun dispatch(command: AutomationCommand): JSONObject {
        if (command.name == "get_state" || command.name == "dump_runtime_state") {
            return currentState()
                .put("ok", true)
                .put("command", command.name)
        }

        rootHandler?.invoke(command)?.let { result ->
            if (result.optBoolean("handled", false)) return result
        }

        editorHandler?.invoke(command)?.let { result ->
            if (result.optBoolean("handled", false)) return result
        }

        return JSONObject()
            .put("ok", false)
            .put("handled", false)
            .put("command", command.name)
            .put("error", "unsupported_or_unavailable_command")
    }

    fun currentState(): JSONObject =
        (editorStateProvider ?: rootStateProvider)?.invoke()
            ?: JSONObject()
                .put("schema_version", "altanova.automation_runtime_state.v0.1")
                .put("active_screen", "unknown")
}
