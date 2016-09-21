package FuseApi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import Volley.VolleyContext;

public class CompanyNameContext {

    private static final String TAG = "CompanyNameContext";

    public void GetCompanyInfo(Context context, String companyName, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) throws FuseException {
        String url = FuseUrl.getCompanyUrl(companyName).buildUpon()
                                                       .build()
                                                       .toString();
        RequestQueue queue = VolleyContext.getInstance(context).getRequestQueue();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, responseListener, errorListener);
        queue.add(jsonObjReq);
    }
}
