package com.example.skucise.activity

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern


class Validator {

    companion object {

        // Default validation messages
        private val PASSWORD_POLICY = """Lozinka mora sadrzati minimum 4 karaktera, od kojih je jedno 
            |veliko slovo, jedno malo slovo, jedan broj i jedan specijalni karakter!""".trimMargin()

        private const val NAME_VALIDATION_MSG = "Ime i prezime mora sadržati najmanje dva karaktera i to karaktere [A-Š]"
        private const val EMAIL_VALIDATION_MSG = "Email nije odgovarajući!"
        private const val PHONE_VALIDATION_MSG = "Broj telefona nije odgovarajući!"
        private const val USERNAME_VALIDATION_MSG = "Username može sadržati najmanje 4 a najviše 12 karaktera i to [a-z] i [0-9]!"

        /**
         * Retrieve string data from the parameter.
         * @param data - can be EditText or String
         * @return - String extracted from EditText or data if its data type is Strin.
         */
        private fun getText(data: Any): String {
            var str = ""
            if (data is EditText) {
                str = data.text.toString()
            } else if (data is String) {
                str = data
            }
            return str
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidName(data: Any, updateUI: Boolean = true): Boolean {

            val p : Pattern = Pattern.compile("[A-zČčĆćŠšžŽĐđ]{2,16}[\\s][A-zČčĆćŠšžŽĐđ]{2,20}")
            val m : Matcher = p.matcher(getText(data))

            // Set error if required
            if (updateUI) {
                val error: String? = if (m.matches()) null else NAME_VALIDATION_MSG
                setError(data, error)
            }

            return m.matches()
            //return valid
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidUsername(data: Any, updateUI: Boolean = true): Boolean {

            val p : Pattern = Pattern.compile("[a-z0-9]{4,12}")
            val m : Matcher = p.matcher(getText(data))

            // Set error if required
            if (updateUI) {
                val error: String? = if (m.matches()) null else USERNAME_VALIDATION_MSG
                setError(data, error)
            }

            return m.matches()
            //return valid
        }

        /**
         * Checks if the email is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the email is valid.
         */
        fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

            // Set error if required
            if (updateUI) {
                val error: String? = if (!TextUtils.isEmpty(str) && valid) null else EMAIL_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the phone is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the phone is valid.
         */
        fun isValidPhone(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = Patterns.PHONE.matcher(str).matches() && str.trim().length > 8 && str.trim().length < 12

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PHONE_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true

            // Password policy check
            // Password should be minimum minimum 8 characters long
            if (str.length < 4) {
                valid = false
            }
            // Password should contain at least one number
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one capital letter
            exp = ".*[A-Z,Š,Đ,Č,Ć,Ž].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one small letter
            exp = ".*[a-z,š,đ,č,ć,ž].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one special character
            // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
            exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?±§].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PASSWORD_POLICY
                setError(data, error)
            }

            return valid
        }

        /**
         * Sets error on EditText or TextInputLayout of the EditText.
         * @param data - Should be EditText
         * @param error - Message to be shown as error, can be null if no error is to be set
         */
        private fun setError(data: Any, error: String?) {
            if (data is EditText) {
                if (data.parent.parent is TextInputLayout) {
                    (data.parent.parent as TextInputLayout).error = error
                } else {
                    data.error = error
                }
            }
        }

    }

}