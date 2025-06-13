package com.ben.aidansdesktopapp.Adapter

import com.ben.aidansdesktopapp.Repository.web.SeleniumWebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/*
* This class should contain access points for the api calls only.
* */
class ApiCallManager(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val coroutineContext:CoroutineContext = Dispatchers.IO
) {

    suspend fun makeLocalSeleniumApiCall(symbol:String){
        return withContext(coroutineContext){
            SeleniumWebService().api(symbol)
        }
    }


}