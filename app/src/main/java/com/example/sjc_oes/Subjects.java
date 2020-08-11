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

class Subjects {
    private ArrayList<String> SubjectList;
    private ArrayList<String> SubjectIDList;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private ContractInterface.Presenter presenter;
    private String CourseID;

    public Subjects(RequestQueue requestQueue, ContractInterface.Presenter presenter) {
        SubjectIDList = new ArrayList<String>();
        SubjectList = new ArrayList<String>();
        this.requestQueue = requestQueue;
        this.presenter = presenter;
        jsonObject = new JSONObject();
    }

    public void DownloadSubjects(){

        try {
            jsonObject.put("CId", CourseID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/FetchSubjects.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                JSONObject jsonObject;
                String string;
                String id;
                SubjectList.clear();
                SubjectIDList.clear();
                try {
                    jsonArray = response.getJSONArray("Subjects");
                    if(jsonArray.length()!=0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            string = jsonObject.getString("SubjectName");
                            id = jsonObject.getString("SubjectId");
                            SubjectList.add(string);
                            SubjectIDList.add(id);
                        }
                        presenter.PresentDataset(SubjectList, "Select the Subject");
                    }
                    else {
                        presenter.showNothingFoundError("No Subject found for the selected Course, Select Other Course",1);
                    }
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

    public String getSubjectName(int position){ return SubjectList.get(position);}
    public String getSubjectId(int position){ return SubjectIDList.get(position);}
    public void setCourseID(String courseID) {
        CourseID = courseID;
    }
    public ArrayList<String> getSubjectList(){
        return SubjectList;
    }

}
