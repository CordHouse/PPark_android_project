package com.example.login;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReserveRequest extends StringRequest {
    final static private String URL = "http://qkqktl5310.ivyro.net/Reserve.php";
    final private Map<String, String> map;

    public ReserveRequest(String userID, int userReserve, String state, String time, int penalty, String penaltyTime,String safeLock, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userReserve", userReserve+"");
        map.put("state", state);
        map.put("time", time);
        map.put("penalty", penalty+"");
        map.put("penaltyTime", penaltyTime);
        map.put("safeLock", safeLock);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
