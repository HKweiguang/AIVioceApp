package com.shimmer.lib_base.base.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getViewHolder(parent: ViewGroup, @LayoutRes layoutId: Int): CommonViewHolder {
            return CommonViewHolder(
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            )
        }
    }

    private val mViews: SparseArray<View?> = SparseArray()

    fun getView(@IdRes viewId: Int): View {
        var view = mViews[viewId]
        return if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)

            view
        } else {
            view
        }
    }

    fun setText(@IdRes viewId: Int, text: String) {
        (getView(viewId) as TextView).text = text
    }

    fun setImageResource(viewId: Int, resId: Int) {
        (getView(viewId) as ImageView).setImageResource(resId)
    }
}