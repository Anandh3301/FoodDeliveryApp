package com.capgemini.firebasedemo.AppData.Restaurants


data class CityItem(
    val locations: List<Location>,
    val name: String="",
    val phone: String=""
)
{
    constructor() : this(mutableListOf(Location()),"","")

}