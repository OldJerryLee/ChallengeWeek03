package com.fabriciopujol.challengethirdweek.models

import java.io.Serializable

data class Place(val title: String, val descrition: String, val latitude: Double, val longitude: Double) : Serializable