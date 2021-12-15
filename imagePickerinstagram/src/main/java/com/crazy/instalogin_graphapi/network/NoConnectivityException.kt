package com.crazy.instalogin_graphapi.network


import android.content.Context
import com.crazy.instalogin_graphapi.R
import java.io.IOException

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class NoConnectivityException(val context: Context) : IOException() {

    override fun getLocalizedMessage(): String {
        return context?.getString(R.string.msg_no_internet_connection)!!
    }

}
