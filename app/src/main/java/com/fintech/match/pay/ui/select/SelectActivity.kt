package com.fintech.match.pay.ui.select


import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.fintech.lxf.helper.toActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import com.fintech.match.pay.R
import com.fintech.match.pay.api.Constants
import com.fintech.match.pay.common.Configuration
import com.fintech.match.pay.common.Constant.ALL_PERMISSION
import com.fintech.match.pay.common.Constant.PERMISSIONS_GROUP
import com.fintech.match.pay.common.route.Router
import com.fintech.match.pay.common.util.Utils
import com.fintech.match.pay.ui.config.ConfigActivity
import com.fintech.match.pay.ui.init.InitActivity
import com.fintech.match.pay.ui.me.MeActivity
import com.fintech.match.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class SelectActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Configuration.noAddress()) {
            Router.get().build(ConfigActivity::class.java).go(this)
            finish()
        } else {
//            Constants.baseUrl = "https://" + Configuration.getUserInfoByKey(Constants.KEY_ADDRESS) + "/"
            Constants.baseUrl = Configuration.getUserInfoByKey(Constants.KEY_ADDRESS) + "/"
        }

        btnInit.setOnClickListener { toActivity(InitActivity::class.java) }
        btnObserver.setOnClickListener { toActivity(MeActivity::class.java) }

//        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + packageName))
//            startActivityForResult(intent, 10)
//        }
    }

    override fun onResume() {
        super.onResume()

        requestAllPermission()
    }


    @AfterPermissionGranted(ALL_PERMISSION)
    fun requestAllPermission() {
        if (!EasyPermissions.hasPermissions(this, *PERMISSIONS_GROUP)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_message),
                    ALL_PERMISSION, *PERMISSIONS_GROUP)
        }
//        else {
//            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
////                Utils.goPermissionSetting(this)
//                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + packageName))
//                startActivityForResult(intent, 10)
//            }
//        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Utils.goPermissionSetting(this)
        }
    }

}
