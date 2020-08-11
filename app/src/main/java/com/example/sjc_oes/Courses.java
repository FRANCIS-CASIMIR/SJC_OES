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

class Courses {
    private ArrayList<String> CourseList;
    private ArrayList<String> CourseIDList;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private ContractInterface.Presenter presenter;
    private String DepartmentID;

    public Courses(RequestQueue requestQueue, ContractInterface.Presenter presenter) {
        CourseList = new ArrayList<String>();
        CourseIDList = new ArrayList<String>();
        this.requestQueue = requestQueue;
        this.presenter = presenter;
        jsonObject = new JSONObject();
    }

    public void DownloadCourses(){

        try {
            jsonObject.put("DeptId", DepartmentID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/FetchCourses.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                JSONObject jsonObject;
                String string;
                String id;
                CourseIDList.clear();
                CourseList.clear();
                try {
                    jsonArray = response.getJSONArray("Courses");

                    if(jsonArray.length()!=0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            string = jsonObject.getString("Name");
                            id = jsonObject.getString("CourseId");
                            CourseList.add(string);
                            CourseIDList.add(id);
                        }
                        presenter.PresentDataset(CourseList, "Select the Course");
                    }
                    else {
                        presenter.showNothingFoundError("No Course found for the selected Department, Select Other Department",0);
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

    public String getCourseId(int index){
        return CourseIDList.get(index);
    }
    public String getCourseName(int index){return CourseList.get(index);}

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }
    public ArrayList<String> getCourseList(){
        return CourseList;
    }
}
