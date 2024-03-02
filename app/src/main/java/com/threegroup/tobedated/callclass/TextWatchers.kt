package com.threegroup.tobedated.callclass

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class TextWatchers {

    //NAME WATCHER
    fun simpleWatcher(btn: Button): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btn.isEnabled = !s.isNullOrEmpty()
            }
        }
    }
    //PHONE NUMBER WATCHER
    fun phoneNumberWatcher(userLoginNumber: EditText, next:Button) : TextWatcher {
        return object : TextWatcher {
            private var editedFlag = false
            private val maxPhoneNumberLength = 10
            private var cursorPosition = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                cursorPosition = userLoginNumber.selectionStart
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (!editedFlag) {
                        editedFlag = true
                        val formattedPhone = formatPhoneNumber(s.toString())
                        userLoginNumber.setText(formattedPhone)
                        val diff = formattedPhone.length - s.length
                        val newPosition = cursorPosition + diff
                        userLoginNumber.setSelection(newPosition.coerceIn(0, formattedPhone.length))
                    } else {
                        editedFlag = false
                    }
                }
                next.isEnabled = s?.length!! >= 14
            }
            //Formats phone number to make it so it looks like a phone number
            private fun formatPhoneNumber(phone: String): String {
                val digits = phone.filter { it.isDigit() }
                val formatted = buildString {
                    if (digits.isNotEmpty()) {
                        append("(")
                        append(digits.take(3))
                        if (digits.length > 3) {
                            append(") ")
                            append(digits.substring(3, minOf(digits.length, 6)))
                        }
                        if (digits.length > 6) {
                            append("-")
                            append(digits.substring(6, minOf(digits.length, maxPhoneNumberLength)))
                        }
                    }
                }
                return formatted
            }
        }
    }

    private var bioCount:Int = 0
    fun charCounter(charaterCount: TextView, nextBTN: Button): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    // Update the character count only if characters are added
                    if (s.length > bioCount) {
                        bioCount = s.length
                    } else if (s.length < bioCount) {
                        // Character(s) have been deleted, decrement the count
                        bioCount = s.length
                    }
                }
                charaterCount.text = "$bioCount/500"
                nextBTN.isEnabled = bioCount >= 15
            }
        }
    }

}