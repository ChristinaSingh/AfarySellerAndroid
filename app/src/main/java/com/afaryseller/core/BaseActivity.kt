package com.afaryseller.core

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.DialogTitle
import androidx.databinding.ViewDataBinding




/*
* All Activities will extend BaseActivity to avoid some repeated code
* */
open class BaseActivity<VB : ViewDataBinding,VM : BaseViewModel> : AppCompatActivity() {

    var mProgressDialog: ProgressDialog? = null
    fun showProgressDialog(
        context: Context?,
        isCancelable: Boolean,
        message: String?
    ): Dialog? {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.show()
        mProgressDialog!!.setCancelable(isCancelable)
        return mProgressDialog
    }

    fun pauseProgressDialog() {
        try {
            if (
                mProgressDialog != null) {
                mProgressDialog!!.cancel()
                mProgressDialog!!.dismiss()
                mProgressDialog = null
            }
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
    }

     fun hideKeyboard(view: View) {
        try {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        } catch (ignored: Exception) {
        }
    }

    fun getFirstNamelastName(displayNames: String?): ArrayList<String> {
        val displayName=displayNames
        val returnArr : ArrayList<String> = arrayListOf()
        if(!displayName.equals(""))
        { val arr = displayName!!.split(" ")
            if(arr.size>1){
                returnArr.add(arr[0])
                returnArr.add(arr[arr.size-1])
            }else if(arr.size>0){
                returnArr.add(arr[0])
            }
        }
        return returnArr
    }






    //getEmailFromContactId
    private fun getEmail(contactId: Int): String? {
        var emailStr = ""
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Email.ADDRESS,  // use
            // Email.ADDRESS
            // for API-Level
            // 11+
            ContactsContract.CommonDataKinds.Email.TYPE
        )
        val email = managedQuery(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection,
            ContactsContract.Data.CONTACT_ID.toString() + "=?",
            arrayOf(contactId.toString()),
            null
        )
        if (email.moveToFirst()) {
            val contactEmailColumnIndex =
                email.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (!email.isAfterLast) {
                // emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";"
                emailStr = emailStr + email.getString(contactEmailColumnIndex)
                email.moveToNext()
            }
        }
        email.close()
        return emailStr
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    /////////////////

    var isAppInFg = false
    var isScrInFg = false

    var isChangeScrFg = false

    var isCheckMyApp :String= "false"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStart() {
        if (!isAppInFg) {
            isAppInFg = true
            isChangeScrFg = false
        } else {
            isChangeScrFg = true
        }
        isScrInFg = true
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (!isScrInFg || !isChangeScrFg) {
            isAppInFg = false
            onAppPause()
        }
        isScrInFg = false
    }



    open fun onAppPause() {
        //remove this toast
       // Toast.makeText(applicationContext, "App in background", Toast.LENGTH_LONG).show()
        // your code
    }


    //this Function used to get Introze User userId


/*
    fun showAlertDialog(message: String,title: String) {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_dialog, null)

        dialogBuilder.setView(dialogView)
        val btnPhone = dialogView.findViewById<View>(R.id.btnPhone) as AppCompatTextView
        val txtMsg = dialogView.findViewById<View>(R.id.txtMsgAlert) as AppCompatTextView
        val textView29 = dialogView.findViewById<View>(R.id.textView29) as AppCompatTextView
        val alertDialog = dialogBuilder.create()
        txtMsg.text = message
        textView29.text = title

        btnPhone.setOnClickListener{
            alertDialog.dismiss()
        }
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 70)
        alertDialog.getWindow()?.setBackgroundDrawable(inset)
        alertDialog.show()
    }
*/

}