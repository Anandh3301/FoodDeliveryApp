package com.capgemini.deliveryapp.Repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.capgemini.firebasedemo.AppData.Menu.Food
import com.capgemini.firebasedemo.AppData.Menu.itemlist

class DBWrapper(val context: Context) {
    val helper = DBHelper(context)
    val db = helper.writableDatabase

    //Called on login/register.Adds a new row/item to CART.Returns no of rows
    //affected by db operation(should be 1)
    fun saveCart(itemtag: String, itemname: String, quantity: Int = 0, price: Int = 0): Long {
        val cvaluess = ContentValues()
        cvaluess.put(DBHelper.CLM_ITEMTAG, itemtag)
        cvaluess.put(DBHelper.CLM_ITEMNAME, itemname)
        cvaluess.put(DBHelper.CLM_QUANTITY, quantity)
        cvaluess.put(DBHelper.CLM_PRICE, price)
        return db.insert(DBHelper.TABLE_NAME, null, cvaluess)

    }

    //Returns a cursor with all items in CART.Iterate over the cursor to retrieve data
    fun retrieveCart(): Cursor {
        val clms = arrayOf(
            DBHelper.CLM_ITEMTAG,
            DBHelper.CLM_ITEMNAME,
            DBHelper.CLM_QUANTITY,
            DBHelper.CLM_PRICE
        )
        return db.query(DBHelper.TABLE_NAME, clms, null, null, null, null, null)
    }

    //TODO put the cursor stuff into a list and return it
    //Returns an arraylist of type <Food>(see Item.kt) with all CART items
    fun retrieveCartAsList(): ArrayList<Food> {
        val cursor = retrieveCart()
        val listofFood = ArrayList<Food>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                Log.wtf(
                    "cursor",
                    "${cursor.getString(0)} : ${cursor.getString(1)} ${cursor.getString(2)}"
                )
                val tag = cursor.getString(0)
                val name = cursor.getString(1)
                val quantity = cursor.getInt(2)
                val price = cursor.getInt(3)
                if (quantity > 0) {
                    listofFood.add(Food(name, quantity, (price).toString(), tag))
                }
            } while (cursor.moveToNext())


        }
        return listofFood
    }

    //Returns a comma separated name-quantity-price,name-quantity-price.... string
    //of CART items
    fun retrieveCartAsString(): String {
        var s = ""
        val cursor = retrieveCart()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                Log.wtf(
                    "cursor",
                    "${cursor.getString(0)} : ${cursor.getString(1)} ${cursor.getString(2)}"
                )
                val tag = cursor.getString(0)
                val name = cursor.getString(1)
                val quantity = cursor.getInt(2)
                val price = cursor.getInt(3)
                if (quantity > 0) {
                    s = s + "$name-$quantity-${price * quantity}"
                    s += ","
                }
            } while (cursor.moveToNext())


        }
        return s.dropLast(1)

    }

    //Pass the item tag as(ex- “item0”).Increments quantity by 1 for passed item.
    //Call on plus button
    fun incrementQuantity(itemtag: String) {   //UPDATE Products SET Price = Price + 50 WHERE ProductID = 1
        // UPDATE {Table} SET {Column} = {Column} + {Value} WHERE {Condition}
        db.execSQL("UPDATE CART SET quantity = quantity + 1 WHERE itemtag = '${itemtag}' ")

    }

    //Returns quantity of specific item in CART
    fun getQuantityofTag(itemtag: String): Int {
        var quantity = 0
        val cursor = retrieveDataFromTag(itemtag)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            quantity = cursor.getInt(2)


        }
        return quantity

    }

    //Returns Σ(quantity) in CART
    fun getTotalQuantity(): Int {
        var quantity = 0
        val cursor = retrieveCart()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                Log.wtf(
                    "cursor",
                    "${cursor.getString(0)} : ${cursor.getString(1)} ${cursor.getString(2)}"
                )


                quantity += cursor.getInt(2)


            } while (cursor.moveToNext())


        }
        return quantity
    }

    //get price for tag (ex-item0)
    fun getPriceofTag(itemtag: String): Int {
        var price = 0
        val cursor = retrieveDataFromTag(itemtag)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            price = cursor.getInt(3)


        }
        return price

    }

    //Pass the item tag as(ex- “item0”).Decrementquantity by 1 for passed item. Call
    //on minus button
    fun decrementQuantity(itemtag: String) {
        db.execSQL("UPDATE CART SET quantity = quantity -1 WHERE itemtag = '${itemtag}' ")

    }

    //Updates quantity of tag(ex-item0) to count
    fun updateQuantity(itemtag: String, count: Int) {
        db.execSQL("UPDATE CART SET quantity = ${count} WHERE itemtag = '${itemtag}'")
    }

    //Updates all quantities to 0 in CART.Called when user checks out
    fun updateAllQuantitytoZero() {
        db.execSQL("UPDATE CART SET quantity = 0")
    }

    //Pass the item tag(ex-”item0”) Returns a cursor with single row
    fun retrieveDataFromTag(itemtag: String): Cursor {
        val clms = arrayOf(
            DBHelper.CLM_ITEMTAG,
            DBHelper.CLM_ITEMNAME,
            DBHelper.CLM_QUANTITY,
            DBHelper.CLM_PRICE
        )
        return db.query(DBHelper.TABLE_NAME, clms, "itemtag='${itemtag}'", null, null, null, null)

    }

    //Deletes CART. call on logout
    fun deleteAll() {
        Log.d("database", "delete all called")
        db.delete(DBHelper.TABLE_NAME, null, null)
    }

    //Makes a firebaseWrapper call to retrieve items from realtime database,
//populates the db on login/register
    fun addRowsFromFirebase() {
        val fbwrapper = FireBaseWrapper()
        fbwrapper.getMenu {
            Log.d("addrowsfromfirebase", it.toString())
            // saveCredentials(it.)
            it.forEach {
                itemlist.add(it)
                saveCart(it.tag, it.item, 0, it.price)
            }
        }
    }

    //Return total amount calculated from db Σ(quantity)*(price)
    fun calculateBill(): Int {
        var sum = 0
        val cursor = retrieveCart()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                var quantity = cursor.getInt(2)
                var price = cursor.getInt(3)
                sum = sum + quantity * price

            } while (cursor.moveToNext())
            //Log.wtf("sumcal", "${cursor.getString(0)} : ${cursor.getString(1)} ${cursor.getString(2)} : ${cursor.getString(3)}")


        }
        return sum
    }

    // (quantity)*(price) for a single item (ex- item0)
    fun calculateBillForSingleItem(itemtag: String): Int {
        var sum = 0
        val cursor = retrieveDataFromTag(itemtag)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            var quantity = cursor.getInt(2)
            var price = cursor.getInt(3)
            sum = quantity * price
        }
        return sum
    }

}
