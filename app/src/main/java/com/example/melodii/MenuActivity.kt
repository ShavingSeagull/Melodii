package com.example.melodii

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.util.*

class MenuActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val permsAccepted: Boolean = prefs.getBoolean("permsAccepted", false)

        if (!permsAccepted) {
            showPermissionsDialog()
        }
    }

    private fun hasReadExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        for(i in permissionsToRequest) {
            Log.d("PermArray", "Array: $i")
        }

        if(!hasReadExternalStoragePermission()) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE && grantResults.isNotEmpty()) {
            for(i in grantResults.indices) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionsRequest", "${permissions[i]} granted.")
                    val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = prefs.edit()
                    editor.putBoolean("permsAccepted", true)
                    editor.apply()
                }
            }
        }
    }

    private fun showPermissionsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions")
            .setMessage("After you close this message you will be asked to grant permission " +
                "to access your device's storage. This is necessary to be able to access your " +
                "music. Please accept this request otherwise the app will be unusable. You will " +
                "only have to do this once, unless you erase your app data.")
            .setPositiveButton("OK", {
                dialog, position -> dialog.dismiss();
                requestPermissions()
            })
            .create().show()

        val prefs: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        var dialogNum: Int = prefs.getInt("dialogNum", -1)
        if(dialogNum <= 0) {
            dialogNum = 1
        } else {
            dialogNum += 1
        }
        // TODO: IF DIALOGNUM > 2 THEN DELETE APP DATA TO RESET THE PERMISSIONS REQUEST
        editor.putInt("dialogNum", dialogNum)
        editor.apply()
    }
}