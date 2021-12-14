package com.burlakov.week1application.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.burlakov.week1application.MyApplication
import com.burlakov.week1application.R
import com.burlakov.week1application.broadcastReceivers.BatteryReceiver
import com.burlakov.week1application.util.Constants
import com.burlakov.week1application.viewmodels.GalleryViewModel
import com.burlakov.week1application.viewmodels.MenuViewModel
import com.google.android.material.navigation.NavigationView
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class MenuActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var username: TextView
    private lateinit var path: String
    private val menuViewModel: MenuViewModel by viewModel()
    private val galleryViewModel: GalleryViewModel by viewModel()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var bReceiver: BatteryReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        bReceiver = BatteryReceiver(drawerLayout)

        registerReceiver(bReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val hView: View = navView.getHeaderView(0)
        username = hView.findViewById(R.id.username)

        if (!MyApplication.checkSavedUserState(this)) {
            navController.navigate(R.id.to_login)
        } else {
            unlockDrawerAndSetName()
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main, R.id.nav_history, R.id.nav_map, R.id.nav_favorite, R.id.nav_gallery,
                R.id.nav_settings, R.id.nav_login
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.nav_photo).setOnMenuItemClickListener {

            drawerLayout.closeDrawers()
            menuViewModel.createImageFile()
            true
        }
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {

            drawerLayout.closeDrawers()
            MyApplication.logOut(this)
            navController.navigate(R.id.to_login)
            true
        }

        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val uri = Uri.fromFile(File(path))
                        UCrop.of(
                            uri,
                            Uri.fromFile(
                                File(
                                    Constants.internalImageDirectory,
                                    "JPEG_${Constants.timeStamp}.jpg"
                                )
                            )
                        )
                            .start(this)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

        val getContent =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                navController.currentDestination?.id?.let { navView.setCheckedItem(it) }
                if (success) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.edit_photo))
                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show()
                } else {
                    File(path).delete()
                }
            }
        menuViewModel.savedImage.observe(this) {
            if (it != null) {
                val image = it
                path = image.absolutePath
                val photoURI = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName.toString() + ".provider",
                    image
                )
                menuViewModel.clear()
                getContent.launch(photoURI)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            galleryViewModel.deleteAndRefresh(File(path))
        }
    }

    fun lockDrawer() {
        toolbar.navigationIcon = null
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unlockDrawerAndSetName() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        username.text = MyApplication.curUser?.username ?: "Undefined"
    }

    override fun onDestroy() {
        unregisterReceiver(bReceiver)
        super.onDestroy()
    }
}