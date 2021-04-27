package com.capgemini.firebasedemo.AppData

import android.util.Log
import com.capgemini.firebasedemo.AppData.Menu.Item
import com.capgemini.firebasedemo.AppData.Menu.itemlist
import com.capgemini.firebasedemo.AppData.Restaurants.CityItem
import com.capgemini.firebasedemo.AppData.Restaurants.Location
import com.google.firebase.database.*

interface MyMenuCallback {
    fun onCallback(itemlist: List<Item>)
}



class FireBaseWrapper {
    fun getMenu(mycallback:MyMenuCallback){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("menu/item")
        val listofItems= mutableListOf<Item>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.children.forEach {
                    Log.d("menu", it.toString())
                    val i = it.getValue(Item:: class.java)

                    if (i != null) {
                        listofItems.add(i)
                        itemlist.add(i)


                    }


                }
                mycallback.onCallback(listofItems)

            }
        })


    }

    fun getMenu(callbackfun : (List<Item>)->(Unit)){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("menu/item")
        val listofItems= mutableListOf<Item>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.children.forEach {
                    Log.d("menu", it.toString())
                    val i = it.getValue(Item:: class.java)

                    if (i != null) {
                        listofItems.add(i)
                        itemlist.add(i)


                    }


                }
               // mycallback.onCallback(listofItems)
                callbackfun(listofItems)

            }
        })


    }
    fun getLocationsOfCity(city: String,callbackfun: (List<Location>) -> Unit)
    {   Log.d("city", "in here")
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("city")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {

            snapshot.children.forEach {
                Log.d("city1",it.toString())
                val city = it.getValue(CityItem::class.java)
               Log.d("city.toString",city.toString())

                    Log.d("aaa", city?.locations!![0].address)

                if(city!=null) {
                    if (city.name.equals("bangalore"))
                    {  Log.d("citypicked", city.locations.toString())
                    callbackfun(city.locations)
                    }
                }

            }


            }
        })


    }
    //fun creatUserWithCredentials()
}
/*
/ Read from the database
myRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String value = dataSnapshot.getValue(String.class);
        Log.d(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
    }
});


 */