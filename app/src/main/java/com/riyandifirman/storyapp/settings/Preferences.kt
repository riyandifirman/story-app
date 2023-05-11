package com.riyandifirman.storyapp.settings

import android.content.Context
import android.content.SharedPreferences

class Preferences (context: Context) {
    val login = "login"
    val myPref = "myPref"
    val myToken = "Bearer"
    val myPreferences : SharedPreferences

    init {
        myPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean){
        myPreferences.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean{
        return myPreferences.getBoolean(login,false)
    }

    fun saveUserToken(token: String){
        myPreferences.edit().putString(myToken,token).apply()
    }

    fun getUserToken(): String? {
        return myPreferences.getString(myToken," ")
    }

    fun clearUserToken(){
        myPreferences.edit().remove(myToken).apply()
    }

    fun clearUserLogin(){
        myPreferences.edit().remove(login).apply()
    }
}