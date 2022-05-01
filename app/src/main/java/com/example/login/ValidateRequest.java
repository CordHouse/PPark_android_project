package com.example.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final private static String URL= "http://qkqktl5310.ivyro.net/Validate.php";
    private Map<String, String> map;

    public ValidateRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener,null);

        map = new HashMap<>();
        map.put("userID", userID);    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}