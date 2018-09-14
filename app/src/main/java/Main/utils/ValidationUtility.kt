package utils

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.example.sarwan.final_year_project.R


class ValidationUtility{

    companion object {
        fun isValidEmail(mEditText: EditText): Boolean {
            val text = mEditText.text.trim().toString()
            if (text.isEmpty()) {
                return false
            } else {
                val correct = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
                if (!correct) {
                    return false
                } else {
                    if (text.contains("example")) {
                        return false
                    }
                }


            }
            return true
        }

        fun isValidEmailForNext(mEditText: EditText): Boolean {
            val text = mEditText.text.trim().toString()
            if(!mEditText.text.isEmpty()){
                val correct = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
                if (!correct) {
                    return false
                }
            }
            else {
                if (text.contains("example")) {
                    return false
                }
            }

            return true
        }

        fun edittextValidator(vararg mEditTexts: EditText): Boolean {
            for (mEditText in mEditTexts) {
                if (mEditText.text.toString().isEmpty()) {
                    mEditText.error = "Required"
                    return false
                }
            }
            return true
        }
        fun textInputLayoutValidator(vararg mTextLayouts: TextInputLayout): Boolean {
            for (mTextLayout in mTextLayouts) {
                if (mTextLayout.editText?.text.toString().isEmpty()) {
                    mTextLayout.error = "Required"
                    removeErrors(mTextLayout)
                    return false
                }
            }
            return true
        }

        fun matchesPassword(password : EditText, confirmPassword : EditText) : Boolean{
            return (password.text.toString().equals(confirmPassword.text.toString()))
        }

        fun isValidPassword(edPassword: EditText): Boolean {
            return edPassword.text.toString().length >= 8
        }
        fun isValidUsername(userName: EditText): Boolean {
            return userName.text.toString().length >= 5
        }

        fun removeErrors(vararg mTextLayouts: TextInputLayout) {
            for (mTextLayout in mTextLayouts) {
                mTextLayout.editText?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        mTextLayout.setError(null)
                    }

                    override fun afterTextChanged(s: Editable) {
                    }
                })
            }
        }

        fun removeErrors(vararg mEditText: EditText) {
            for (EditText in mEditText) {
                EditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        EditText.setError(null)
                    }

                    override fun afterTextChanged(s: Editable) {
                    }
                })
            }
        }

        fun removeErrors(vararg textView: TextView) {
            for (mTextView in textView) {
                mTextView.error = null
            }
        }

        fun setError(mTextLayout: TextInputLayout , error: String){
            mTextLayout.error = error
        }

        fun setEditTextError(error: String , vararg mEditText: EditText){
            for (EditText in mEditText) {
                EditText.error = error
            }
        }

        fun appendAtRunTime(mTextLayout: TextInputLayout,toAppend : String,afterIndex : Int) {
            mTextLayout.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.length==afterIndex){
                        mTextLayout.editText?.append(toAppend)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    if(s.length==afterIndex){
                        mTextLayout.editText?.append(toAppend)
                    }
                }
            })
        }
    }
}