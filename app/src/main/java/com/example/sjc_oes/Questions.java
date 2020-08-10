package com.example.sjc_oes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Questions {

    private ContractInterface2.QuestionPresenterInterface questionPresenterInterface;
    private JSONObject jsonObject;
    private RequestQueue requestQueue;
    private ArrayList<String> QuestionList,Choice1,Choice2,Choice3,Choice4;
    private int[] Responses,Answers,Type;
    private int Mark = 0,question_number=0;
    Integer[] randomorder;
    List<Integer> integerList;
    String SubjectId;


    public Questions(RequestQueue requestQueue, ContractInterface2.QuestionPresenterInterface questionPresenterInterface) {
        this.questionPresenterInterface = questionPresenterInterface;
        QuestionList = new ArrayList<String>();
        Choice1 = new ArrayList<>();
        Choice2 = new ArrayList<>();
        Choice3 = new ArrayList<>();
        Choice4 = new ArrayList<>();
        jsonObject = new JSONObject();
        question_number = 0;
        SubjectId = null;

        Responses = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Answers = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Type = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        this.requestQueue = requestQueue;

        randomorder = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        integerList = Arrays.asList(randomorder);
        Collections.shuffle(integerList);
        integerList.toArray(randomorder);
        System.out.println("-------------------------------------->"+Arrays.toString(randomorder));
    }

    private void DownloadQuestions(){

        try {
            jsonObject.put("SubjectId",this.SubjectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/FetchQuestions.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);

                QuestionList.clear();Choice1.clear();Choice2.clear();Choice3.clear();Choice4.clear();
                JSONObject jsonObject1;
                JSONArray jsonArray;
                int type=0;
                int ans=0;
                String Qs="",c1="",c2="",c3="",c4="";
                try {
                    jsonArray = response.getJSONArray("Questions");

                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject1 = jsonArray.getJSONObject(i);
                        Qs = jsonObject1.getString("Question");
                        c1=jsonObject1.getString("Choice1");c2=jsonObject1.getString("Choice2");
                        c3=jsonObject1.getString("Choice3");c4=jsonObject1.getString("Choice4");
                        type=jsonObject1.getInt("Type");
                        ans=jsonObject1.getInt("Answer");
                        Type[i]=type;
                        Answers[i]=ans;
                        QuestionList.add(Qs);
                        //System.out.println("Type===============================>"+type);
                        Choice1.add(c1);Choice2.add(c2);Choice3.add(c3);Choice4.add(c4);
                    }
                    questionPresenterInterface.updateFirstQuestion(Type[randomorder[question_number]],QuestionList.get(randomorder[question_number]),
                            Choice1.get(randomorder[question_number]),Choice2.get(randomorder[question_number])
                            ,Choice3.get(randomorder[question_number])
                            ,Choice4.get(randomorder[question_number]));
                } catch (JSONException e) {
                    e.printStackTrace();
                    questionPresenterInterface.Reporterror("Network Error. Restart your app.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                questionPresenterInterface.Reporterror("Error.Try Restarting the App");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }//ok

    public void UpdateResponsestodb(String SubjectId,String Dnumber){

        // Preparing data to be Updated
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("SubjectId",SubjectId);
            responseObject.put("Dnumber",Dnumber);
            responseObject.put("q1",Responses[0]);
            responseObject.put("q2",Responses[1]);
            responseObject.put("q3",Responses[2]);
            responseObject.put("q4",Responses[3]);
            responseObject.put("q5",Responses[4]);
            responseObject.put("q6",Responses[5]);
            responseObject.put("q7",Responses[6]);
            responseObject.put("q8",Responses[7]);
            responseObject.put("q9",Responses[8]);
            responseObject.put("q10",Responses[9]);
            responseObject.put("q11",Responses[10]);
            responseObject.put("q12",Responses[11]);
            responseObject.put("q13",Responses[12]);
            responseObject.put("q14",Responses[13]);
            responseObject.put("q15",Responses[14]);
            responseObject.put("Mark",Mark);
        } catch (JSONException e) {
            e.printStackTrace();
            questionPresenterInterface.ReportUpdateError();
        }

        //Hitting the backend
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://onlineexamsystemsjc.000webhostapp.com/UpdateStatusandmark.php", responseObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                questionPresenterInterface.startFinalActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                questionPresenterInterface.ReportUpdateError();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public String getQuestionList() {
        return QuestionList.get(randomorder[question_number]);
    }

    public String getChoice1() {
        return Choice1.get(randomorder[question_number]);
    }

    public String getChoice2() {
        return Choice2.get(randomorder[question_number]);
    }

    public String getChoice3() {
        return Choice3.get(randomorder[question_number]);
    }

    public String getChoice4() {
        return Choice4.get(randomorder[question_number]);
    }

    public int getType(){
        return Type[randomorder[question_number]];
    }

    public void updateResponse(int answer){
        Responses[randomorder[question_number]] = answer;
        if(answer==Answers[randomorder[question_number]]){
            Mark++;
        }
        question_number++;
    }
    public int getMark(){
        return Mark;
    }
    public void setSubjectId(String subjectId){
        this.SubjectId = subjectId;
        DownloadQuestions();
    }
}
