package com.rahul.apksharingapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: appAdapter
    private lateinit var userapps : ArrayList<App>
    private lateinit var allapps:ArrayList<App>
    private var applistflag = false
    private lateinit var sortImageView: ImageView
    private lateinit var context:Activity
    private lateinit var loadingdialog:loadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        sortImageView = findViewById(R.id.sort)
        sortImageView.setOnClickListener {

            this.openContextMenu(it)
        }

        context = this@MainActivity

        loadingdialog = loadingDialog(this@MainActivity)
        loadingdialog.startLoadingDialog()




        recyclerView = findViewById(R.id.appList)
        userapps = ArrayList<App>()
        allapps  = ArrayList<App>()


        


        CoroutineScope(IO).launch {
            getData()

        }








      






        registerForContextMenu(sortImageView)
    }

    private suspend fun getData() {
        var user = ArrayList<App>()
        var all  = ArrayList<App>()


        var packageManager:PackageManager = applicationContext.packageManager

        var packages: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for(packageinfo:ApplicationInfo in packages){
            var name:String = packageManager.getApplicationLabel(packageinfo) as String
            var icon:Drawable = packageManager.getApplicationIcon(packageinfo)
            var apkPath = packageinfo.publicSourceDir
            var apkSize = File(packageinfo.publicSourceDir).length()
            all.add(App(name,icon,apkPath,apkSize))

            if (packageinfo.publicSourceDir.startsWith("/data/app/")) {
                user.add(App(name, icon, apkPath, apkSize))
            }




        }
        setData(user,all)

    }
    private suspend fun setData(templist:ArrayList<App>,templist2:ArrayList<App>){
        withContext(Main){

            setviewData(templist,templist2)


        }

    }

    private fun setviewData(templist: ArrayList<App>,templist2:ArrayList<App>) {
        allapps.addAll(templist2)
        userapps.addAll(templist)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        userapps.sortBy { it.name }
        adapter = appAdapter(context,userapps)
        recyclerView.adapter = adapter
        loadingdialog.dismissDialog()

    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflowmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    private  fun updateAdapter(list:ArrayList<App>) {
        adapter = appAdapter(context,list)
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.title == "Show System Apps" || item.title == "Hide System Apps"){
            applistflag = !applistflag

            if(applistflag) updateAdapter(allapps)
            else updateAdapter(userapps)



            if(!applistflag){
                item.setTitle("Show System Apps")
            }
            else{
                item.setTitle("Hide System Apps")
            }
//        }
        return true
    }



    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.setHeaderTitle("Sort By")

        if (v != null) {
            menu?.add(0, v.getId(), 0, "Name")

            menu?.add(0, v.getId(), 0, "Size")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if(item.title == "Name"){
            if(applistflag){
                allapps.sortBy { it.name }
                updateAdapter(allapps)
            }
            else {
                userapps.sortBy { it.name }
                updateAdapter(userapps)

            }

        }
        if(item.title == "Size"){
            if(applistflag){
                allapps.sortBy { it.apkSize }
                updateAdapter(allapps)
            }
            else{
                userapps.sortBy{it.apkSize}
                updateAdapter(userapps)
            }

        }

        return true
    }







}


