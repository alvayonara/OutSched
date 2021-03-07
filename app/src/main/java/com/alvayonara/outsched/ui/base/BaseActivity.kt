package com.alvayonara.outsched.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.utils.Helper.showMaterialDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    private lateinit var permissionListener: MultiplePermissionsListener

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setup()
    }

    abstract fun setup()

    protected fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    protected fun setLog(message: String) = Timber.e(message)

    protected fun requestPermissions(
        permissions: List<String>,
        action: () -> Unit, actionDeny: () -> Unit
    ) {
        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(getPermissionsListener(action, actionDeny))
            .check()
    }

    private fun getPermissionsListener(
        action: () -> Unit, actionDeny: () -> Unit
    ): MultiplePermissionsListener {
        if (!this::permissionListener.isInitialized) {
            permissionListener = object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let { rpt ->
                        if (rpt.areAllPermissionsGranted()) action()
                        else actionDeny()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        }
        return permissionListener
    }

    protected fun showDenyPermission() {
        showMaterialDialog(
            context = this,
            title = R.string.request_permission,
            message = R.string.request_permission_1,
            positiveText = R.string.request_permission_2,
            negativeText = R.string.request_permission_3,
            actionPositive = {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .also { intent ->
                        intent.data = Uri.parse("package:${packageName}")
                        startActivity(intent)
                    }
            },
            actionNegative = { onBackPressed() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}