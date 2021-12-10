package com.burlakov.week1application.activities

import android.content.DialogInterface
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main, R.id.nav_history, R.id.nav_map, R.id.nav_favorite, R.id.nav_gallery,
                R.id.nav_settings
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.nav_photo).setOnMenuItemClickListener {

            drawerLayout.closeDrawers()
            menuViewModel.createImageFile()
            true
        }

        val hView: View = navView.getHeaderView(0)
        username = hView.findViewById(R.id.username)
        username.text = MyApplication.curUser?.username ?: "Undefined"

        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val uri = Uri.fromFile(File(path))
                        UCrop.of(uri, uri)
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
            val image = it
            path = image.absolutePath
            val photoURI = FileProvider.getUriForFile(
                this,
                applicationContext.packageName.toString() + ".provider",
                image
            )
            getContent.launch(photoURI)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }


}