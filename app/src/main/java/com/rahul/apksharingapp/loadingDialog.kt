package com.rahul.apksharingapp

import android.app.Activity
import android.app.AlertDialog

class loadingDialog(var activity:Activity) {
    private lateinit var dialog:AlertDialog

    fun startLoadingDialog(){

        var builder = AlertDialog.Builder(activity)
        var inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.customdialog,null))
        builder.setCancelable(false)

         dialog = builder.create()
        dialog.show()

    }
    fun dismissDialog(){
        dialog.dismiss()
    }
}