package com.example.todolist.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.example.todolist.R

class LoadingUtil() {
    companion object {
        private var mLoadingDialog: Dialog? = null
        private lateinit var mContext: Context

        @JvmStatic
        fun show(context: Context, label: String = "") {
            mContext = context
            if(mLoadingDialog != null) {
                if(mLoadingDialog?.isShowing == true) {
                    mLoadingDialog?.dismiss()
                }

                mLoadingDialog = null
            }

            mLoadingDialog = Dialog(mContext)
            mLoadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mLoadingDialog!!.setContentView(R.layout.custom_dialog_progress)

            val loadingTextView = mLoadingDialog!!.findViewById(R.id.progress_tv) as TextView
            loadingTextView.text = label
            loadingTextView.setTextColor(Color.WHITE)
            loadingTextView.textSize = 12F

            mLoadingDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mLoadingDialog!!.setCancelable(false)
            mLoadingDialog!!.show()
        }

        @JvmStatic
        fun hide() {
            if(mLoadingDialog != null) {
                if(mLoadingDialog?.isShowing == true) {
                    mLoadingDialog?.dismiss()
                }

                mLoadingDialog = null
            }
        }
    }
}