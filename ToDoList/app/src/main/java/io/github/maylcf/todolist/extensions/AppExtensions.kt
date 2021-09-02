package io.github.maylcf.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd/MM/yyyy"
private val locale = Locale("pt", "BR")

fun Date.format(): String {
    return SimpleDateFormat(DATE_FORMAT, locale).format(this)
}

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }