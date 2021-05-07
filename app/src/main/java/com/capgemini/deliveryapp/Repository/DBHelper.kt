package com.capgemini.deliveryapp.Repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "credentials.db", null, 5) {   //CART database
    companion object {

        val TABLE_NAME = "CART"
        val CLM_ITEMTAG = "itemtag"
        val CLM_ITEMNAME = "name"
        val CLM_QUANTITY = "quantity"
        val CLM_PRICE = "price"
        val TABLE_QUERY =
            "create table $TABLE_NAME ($CLM_ITEMTAG text, $CLM_ITEMNAME text ,$CLM_QUANTITY integer, $CLM_PRICE integer)"

    }

    //create database with specified fields
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(TABLE_QUERY)
            Log.wtf("dbhelper", "table created")
        } catch (e: SQLException) {
            Log.wtf("dbhelper", e.message)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}