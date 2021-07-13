package com.rahul.apksharingapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class appAdapter(val context: Activity, val appList: ArrayList<App>) : RecyclerView.Adapter<appAdapter.viewHolder>() {
    class viewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        val size: TextView
        val icon: ImageView
        val layout: ConstraintLayout
        init {
            name = view.findViewById(R.id.name)
            size = view .findViewById(R.id.size)
            icon = view.findViewById(R.id.icon)
            layout = view.findViewById(R.id.onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.approw, parent, false)

        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
            holder.name.text = appList.get(position).name
            holder.size.text = getreadable(appList.get(position).apkSize)
            holder.icon.setImageDrawable(appList.get(position).icon)
            holder.layout.setOnClickListener(View.OnClickListener() {
                var shareapkIntent: Intent = Intent()

                shareapkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                shareapkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                shareapkIntent.setAction(Intent.ACTION_SEND)
                shareapkIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider", File(appList.get(position).apkPath)
                ))
                shareapkIntent.setType("application/vnd.android.package-archieve")
                context.startActivity(Intent.createChooser(shareapkIntent, "Shareapk"))

            })



    }

    private fun getreadable(apkSize: Long): String? {

            var convertednumber:String = ""
            if(apkSize<1024){
                convertednumber = String.format(context.getString(R.string.app_b), apkSize.toDouble())

            }
            else if(apkSize<Math.pow(1024.0, 2.0)){
                convertednumber = String.format(context.getString(R.string.app_kb), (apkSize / 1024).toDouble())

            }
            else if(apkSize<Math.pow(1024.0, 3.0)){
                convertednumber = String.format(context.getString(R.string.app_mb), (apkSize / Math.pow(1024.0, 2.0)).toDouble())

            }
            else if(apkSize<Math.pow(1024.0, 3.0)){
                convertednumber = String.format(context.getString(R.string.app_gb), (apkSize / Math.pow(1024.0, 3.0)).toDouble())

            }
        return convertednumber
    }

    override fun getItemCount() = appList.size
}





















