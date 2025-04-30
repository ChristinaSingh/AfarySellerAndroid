package com.afaryseller.core

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


/*
*
*
*
*/
abstract class BaseFragment<VB : ViewDataBinding, /*Generic type of BaseViewModel*/ VM : BaseViewModel>() :
    Fragment() {
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
                activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
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
            ContactsContract.CommonDataKinds.Email.DATA,  // use
            // Email.ADDRESS
            // for API-Level
            // 11+
            ContactsContract.CommonDataKinds.Email.TYPE
        )
        val email = activity?.managedQuery(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection,
            ContactsContract.Data.CONTACT_ID.toString() + "=?",
            arrayOf(contactId.toString()),
            null
        )
        if (email?.moveToFirst() == true) {
            val contactEmailColumnIndex =
                email?.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
            while (!email.isAfterLast) {
                // emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";"
              //  emailStr += contactEmailColumnIndex?.let { email?.getString(it) } ?:
                email.moveToNext()
            }
        }
        email?.close()
        return emailStr
    }

/*
    fun showAlertDialog(message: String,title: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
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