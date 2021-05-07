package com.capgemini.deliveryapp.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.capgemini.firebasedemo.AppData.FireBaseWrapper

class DBWrapper (val context : Context){
    val helper=DBHelper(context)
    val db=helper.writableDatabase
    fun saveCredentials(itemtag : String,quantity : Int=0,price : Int=0): Long {
        val cvaluess=ContentValues()
        cvaluess.put(DBHelper.CLM_ITEMTAG,itemtag)
        cvaluess.put(DBHelper.CLM_QUANTITY,quantity)
        cvaluess.put(DBHelper.CLM_PRICE,price)
        return db.insert(DBHelper.TABLE_NAME,null,cvaluess)

    }
    fun retrieveCredentials() : Cursor
    {   val clms=arrayOf(DBHelper.CLM_ITEMTAG,DBHelper.CLM_QUANTITY,DBHelper.CLM_PRICE)
       return db.query(DBHelper.TABLE_NAME,clms,null,null,null,null,null)
    }
    fun incrementQuantity(itemtag : String)
   {   //UPDATE Products SET Price = Price + 50 WHERE ProductID = 1
      // UPDATE {Table} SET {Column} = {Column} + {Value} WHERE {Condition}
   db.execSQL("UPDATE CART SET quantity = quantity + 1 WHERE itemtag = '${itemtag}' ")

   }
    fun deleteAll()
    { Log.d("database","delete all called")
        db.delete(DBHelper.TABLE_NAME,null,null)
    }

    fun addRowsFromFirebase()
    {
        val fbwrapper= FireBaseWrapper()
        fbwrapper.getMenu {
            Log.d("addrowsfromfirebase",it.toString())
           // saveCredentials(it.)
            it.forEach {
                saveCredentials(it.tag,0,it.price)
            }
        }
    }
}
