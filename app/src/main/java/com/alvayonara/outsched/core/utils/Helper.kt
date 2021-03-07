package com.alvayonara.outsched.core.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onShow

object Helper {

    fun showMaterialDialog(
        context: Context,
        title: Int,
        message: Int,
        positiveText: Int,
        negativeText: Int,
        actionPositive: () -> Unit,
        actionNegative: () -> Unit
    ) {
        MaterialDialog(context).show {
            title(title)
            message(message)
            positiveButton(positiveText) {
                dismiss()
                actionPositive()
            }
            negativeButton(negativeText) {
                dismiss()
                actionNegative()
            }
            cancelable(false)
            onShow {
                it.getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
                it.getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            }
        }
    }

    fun intentUri(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}