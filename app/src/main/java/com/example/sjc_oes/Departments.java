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

class Departments {

    private ArrayList<String> DepartmentList;
    private ArrayList<String> DepartmentIDList;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private ContractInterface.Presenter presenter;

    public Departments(RequestQueue requestQueue, ContractInterface.Presenter presenter) {
        this.presenter = presenter;
        this.requestQueue = requestQueue;
        DepartmentList = new ArrayList<String>();
        DepartmentIDList = new ArrayList<String>();
    }

    public String getDepartmentId(int index){
        return DepartmentIDList.get(index);
    }

    public String getDepartment(int index){return DepartmentList.get(index);}

    public ArrayList<String> getDepartmentList(){
        return DepartmentList;
    }

    public void DownloadDepartments(){
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/Fetch_Depts.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;
                String string = null;
                String id = null;
                DepartmentList.clear();
                DepartmentIDList.clear();
                try {
                        jsonArray = response.getJSONArray("Departments");

                        if(jsonArray.length()!=0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                string = jsonObject.getString("Name");
                                id = jsonObject.getString("DeptId");
                                DepartmentList.add(string);
                                DepartmentIDList.add(id);
                                System.out.println(string + " " + id);
                            }
                            presenter.PresentDataset(DepartmentList, "Select Your Department");
                        }
                        else {
                            presenter.showNoDepartmentFoundError();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    presenter.Reporterror("Error.Try Restarting the App");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.Reporterror("Unable to connect.Press Refresh");
            }
        });
        requestQueue.add(jsonObjectRequest);
        System.out.println("Fetch Dept Finished");
    }

}
