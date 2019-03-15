package com.dvt_airpots.Helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.dvt_airpots.MainActivity;

import org.apache.http.HttpResponse;
import java.io.IOException;
import java.util.Map;

public class VolleyRequestHelper {

    private static VolleyRequestHelper requestQueueSingleton;

    private RequestQueue requestQueue;
    private static MainActivity mainActivity = null;

    //----------------------------------------------------------------------------------------------
    private VolleyRequestHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        requestQueue = Volley.newRequestQueue(mainActivity.getApplicationContext(), new VolleyHelperHurlStack());
    }

    //----------------------------------------------------------------------------------------------
    public static synchronized VolleyRequestHelper getInstance(MainActivity baseActivity) {
        if (requestQueueSingleton == null) {
            requestQueueSingleton = new VolleyRequestHelper(baseActivity);
        }
        return requestQueueSingleton;
    }

    //----------------------------------------------------------------------------------------------
    public RequestQueue getRequestQueue() {
        if (requestQueue != null) {
            return requestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized"); // throw an exception and create the resource again
        }
    }

    //----------------------------------------------------------------------------------------------
    public static class VolleyHelperHurlStack extends HurlStack {
        @Override
        public HttpResponse performRequest(Request<?> request, Map<String, String> customHeaders) throws IOException, AuthFailureError {


            return super.performRequest(request, customHeaders); // evokes BasicNetwork first then HurlStack
        }
    }
    //----------------------------------------------------------------------------------------------
}
