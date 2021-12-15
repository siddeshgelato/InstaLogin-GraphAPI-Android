package com.crazy.instalogin_graphapi.network


import android.content.Context
import com.crazy.instalogin_graphapi.common.InternetUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class ConnectivityInterceptor(val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!InternetUtils.isInternetConnected(context)) {
            throw NoConnectivityException(context)
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
