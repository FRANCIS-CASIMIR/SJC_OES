package com.example.sjc_oes;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class DNumbers {
    private ArrayList<String> DNumbers,Passwords;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private ContractInterface.Presenter presenter;
    private String SubjectId;

    public DNumbers(RequestQueue requestQueue, ContractInterface.Presenter presenter) {
        DNumbers = new ArrayList<String>();
        Passwords = new ArrayList<String>();
        this.requestQueue = requestQueue;
        this.presenter = presenter;
        SubjectId = null;
        jsonObject = new JSONObject();
    }

    public void DownloadDnumbers(){
        try {
            jsonObject.put("SubjectId", this.SubjectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/FetchDnumbers.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                JSONObject jsonObject;
                String Dnumber,Password;
                DNumbers.clear();
                Passwords.clear();
                try {
                    jsonArray = response.getJSONArray("D_Numbers&Passwords");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Dnumber = jsonObject.getString("DNumber");
                        Password = jsonObject.getString("Password");
                        DNumbers.add(Dnumber);
                        Passwords.add(Password);
                    }
                    presenter.PresentDataset(DNumbers,"Select Your D.Number");
                } catch (JSONException e) {
                    e.printStackTrace();
                    presenter.Reporterror("Error.Try Restarting the App");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.Reporterror("Unable to connect.Press Refresh ");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public String getDNumber(int position){
        return DNumbers.get(position);
    }
    public String getPassword(int position){
        System.out.println("===========================>Password is "+Passwords.get(position));
        return Passwords.get(position);
    }
    public void setSubjectId(String subjectId){
        this.SubjectId=subjectId;
        System.out.println("Dnumer----->Subject Id is seted to ==="+subjectId);
    }
}
