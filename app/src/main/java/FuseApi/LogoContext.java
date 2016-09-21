package FuseApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import Volley.VolleyContext;

public class LogoContext {

    private static final String TAG = "LogoContext";

    public void GetLogo(Context context, String url, Response.Listener<Bitmap> responseListener, Response.ErrorListener errorListener){
        ImageRequest imageRequest= new ImageRequest(url, responseListener, 100,100, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, errorListener);
        RequestQueue queue = VolleyContext.getInstance(context).getRequestQueue();
        queue.add(imageRequest);
    }
}
