package FuseApi;

import android.net.Uri;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FuseUrl {

    private static final String url = ".fusion-universal.com/api/v1/company.json";

    private static final Map<String, String> headers;

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("Content-Type", "application/json");
        headers = aMap;
    }

    public static Uri getCompanyUrl(String companyName) throws FuseException{
        if(companyName == null)throw new FuseException("Company name is null");
        if(companyName.length() <= 0) throw new FuseException("Company name is blank");
        return Uri.parse(MessageFormat.format("https://{0}{1}", companyName.trim(), url));
    }


    public static Map<String, String> getHeaders() {
        return headers;
    }
}
