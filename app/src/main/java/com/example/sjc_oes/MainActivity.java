package com.example.sjc_oes;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContractInterface.View {

    private TextView collegeName,collegePlace,versionString,designString1,designString2,infoString;
    EditText password;
    private Button buttonBack,buttonNext,buttonrefresh;
    private Spinner spinner;
    private ImageView collegeLogo;
    private ArrayList<String> dataList;
    private RequestQueue requestQueue;
    private Intent intent;
    private ArrayAdapter<String> arrayAdapter;
    private PresenterClass presenterClass;
    private int PositionSelected;
    LottieAnimationView loadinganimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        //======================= Listener for next Button =================================================

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoString.setTextColor(Color.BLACK);
                loadinganimation.playAnimation();
                loadinganimation.setVisibility(View.VISIBLE);
                buttonBack.setVisibility(View.INVISIBLE);
                buttonNext.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                presenterClass.onNextButtonPressed(PositionSelected);
            }
        });

        //======================= Listener for back Button =================================================

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoString.setTextColor(Color.BLACK);
                presenterClass.onBackButtonPressed(PositionSelected);
            }
        });


        //======================= Listener for spinner =================================================
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PositionSelected = position;
                System.out.println("*****  SElected POsition is****"+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //======================= Listener for Refresh Button =================================================

        buttonrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoString.setTextColor(Color.BLACK);
                loadinganimation.setVisibility(View.VISIBLE);
                loadinganimation.playAnimation();
                presenterClass.refresh();
                buttonrefresh.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void initComponents(){
        spinner = findViewById(R.id.spinner);
        collegeName = findViewById(R.id.clgname);
        collegePlace = findViewById(R.id.clgplace);
        versionString = findViewById(R.id.versionstring);
        designString1 = findViewById(R.id.design1string);
        designString2 = findViewById(R.id.design2string);
        infoString = findViewById(R.id.infostring);
        buttonBack = findViewById(R.id.button_back);
        buttonNext = findViewById(R.id.button_next);
        buttonrefresh = findViewById(R.id.button_refresh);
        buttonrefresh.setVisibility(View.INVISIBLE);
        collegeLogo = findViewById(R.id.clg_logo);
        password = findViewById(R.id.password);
        password.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        dataList = new ArrayList<String>();;
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinnertext,dataList);
        arrayAdapter.setDropDownViewResource(R.layout.dropdowntext);
        spinner.setAdapter(arrayAdapter);
        loadinganimation = findViewById(R.id.animation);

        buttonBack.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);

        // Initiating download department
        presenterClass = new PresenterClass(requestQueue,this);
    }

    @Override
    public void updateArrayList(ArrayList<String> arrayList) {
        dataList.clear();
        dataList.addAll(arrayList);
        arrayAdapter.notifyDataSetChanged();
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void startIntent(Student student) {
        intent = new Intent(MainActivity.this,QuestionDisplayActivity.class);
        intent.putExtra("student Details",student);
        startActivity(intent);
    }

    @Override
    public void Hidespinner() {
        buttonBack.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        loadinganimation.setVisibility(View.INVISIBLE);
    }

    @Override
    public void unHidespinner() {
        spinner.setVisibility(View.VISIBLE);
        password.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getpasswordfromuser() {
        return password.getText().toString();
    }

    @Override
    public void Showerrormsg(String error) {
        //pause the animation
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);

        // show the message
        infoString.setTextColor(Color.RED);
        infoString.setText(error);
        buttonrefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void ReportPasswordError() {
        infoString.setTextColor(Color.RED);
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNothingFoundError() {
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        infoString.setTextColor(Color.RED);
        buttonBack.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoDepartmentFoundError() {
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        infoString.setTextColor(Color.RED);
        infoString.setText("No Data Found..");
        buttonBack.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String string) {
        infoString.setText(string);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity------>","MainActivity is Destroyed");
    }

}
