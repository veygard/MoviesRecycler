package com.veygard.movies_recycler.util

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.veygard.movies_recycler.R


class CustomToast(
    context: Context,
    message: String,
    bgColor: Int = context.getColor(R.color.blue),
    textColor: Int = context.getColor(R.color.white),
    dur: Int = LENGTH_SHORT,
) :
    Toast(context) {

    init {
        val view: View = View.inflate(context, R.layout.custom_toast, null)
        val txtMsg = view.findViewById<TextView>(R.id.custom_toast_text)
        val card = view.findViewById<CardView>(R.id.custom_toast_card)

        txtMsg.text = message
        txtMsg.setTextColor(textColor)
        card.background.setTint(bgColor)

        setView(view)
        duration = dur
    }
}