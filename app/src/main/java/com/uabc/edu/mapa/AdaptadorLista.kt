package com.uabc.edu.mapa


import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class MessageAdapter(context: Context, resource: Int, objects: List<Puntos>) : ArrayAdapter<Puntos>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = (context as Activity).layoutInflater.inflate(R.layout.renglon, parent, false)
        }

        val messageTextView = convertView!!.findViewById(R.id.fecha) as TextView
        val subtext = convertView!!.findViewById(R.id.posicion) as TextView

        val message = getItem(position)
        messageTextView.setText(message!!.fecha)
        subtext.setText(message!!.longitud +","+message!!.latitud)

        return convertView
    }
}
