package com.example.week_7_task

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// Gets fileName using content resolver
fun ContentResolver.getFileName(uri : Uri) : String{
    var name = ""
    var cursor = query(uri,null, null, null, null)

    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

// Sets the backgroundColor a view containing a pokemon based on the pokemon type
fun View.getPokemonColor(view: View, type: List<Type>){
    val con = view.resources
   val color =  when(type[0].type.name){
        "grass","bug" -> con.getColor(R.color.teal_200)
        "fire" -> con.getColor(R.color.red)
       "water","fighting"->con.getColor(R.color.lightBlue)
       "poison","ghost"->con.getColor(R.color.lightPurple)
       "ground","rock"->con.getColor(R.color.lightBrown)
       "dark","normal"->con.getColor(R.color.black)
       else->con.getColor(R.color.lightBlue)
    }
    this.setBackgroundColor(color)
}

// Navigates to a fragment based on the Id
fun Fragment.goto(id : Int){
    findNavController().navigate(id)
}