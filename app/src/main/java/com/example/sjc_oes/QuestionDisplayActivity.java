package com.example.sjc_oes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class QuestionDisplayActivity extends AppCompatActivity implements ContractInterface2.QuestionView {

    private TextView remainingTimeInfo,remainingTime,QuestionString,loginString,subjectName,Questionnumber;
    private RadioGroup Choices;
    private RadioButton Choice1,Choice2,Choice3,Choice4;
    private Button nextQuestion;
    private Intent intent;
    boolean shouldallowBack,LastQuestionWasType1;
    LottieAnimationView loadinganimation;

    //for type1
    private ImageView question_img,choice1_img,choice2_img,choice3_img,choice4_img;
    private LinearLayout choice1_lyt,choice2_lyt,choice3_lyt,choice4_lyt;
    private RadioButton choice1_rdbtn,choice2_rdbtn,choice3_rdbtn,choice4_rdbtn;

    private QuestionPresenter questionPresenter;
    private RequestQueue requestQueue;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);

        shouldallowBack = false;
        LastQuestionWasType1 = false;
        intent = getIntent();

        loadinganimation = findViewById(R.id.loadinganimation);

        // initializing type0stuff
        init_Type0stuff();
        init_type1stuff();

        //hide the type1 & some other components in order to make a loading feel to the user
        HideType1Stuff();

        Choices.setVisibility(View.INVISIBLE);
        remainingTimeInfo.setVisibility(View.INVISIBLE);
        remainingTime.setVisibility(View.INVISIBLE);
        nextQuestion.setVisibility(View.INVISIBLE);


       //request que
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        toast =  Toast.makeText(this,"Choose any choice..",Toast.LENGTH_SHORT);

        // fetching first question
        questionPresenter = new QuestionPresenter(requestQueue,this,(Student) intent.getParcelableExtra("student Details"));

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPresenter.updateNextQuestion();
            }
        });
        init_listenersoftype0();
        init_listenersoftype1();
    }

    @Override
    public void makeToast() {
            toast.show();
    }

    @Override
    public void setStrings(String SubjectName, String Dnumber,boolean IsQuestionType0) {
        if(IsQuestionType0){
            LastQuestionWasType1 = true;
        }
        loginString.setText("Logged in As :"+Dnumber);
        subjectName.setText("Subject Name :"+SubjectName);
        remainingTimeInfo.setVisibility(View.VISIBLE);
        remainingTime.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startFeedbackActivity(Student student) {
        intent = new Intent(QuestionDisplayActivity.this,MainActivity.class);

        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    @Override
    public void clearCheck() {
        Choices.clearCheck();
    }

    @Override
    public void updateQuestion_and_Choices(String question, String choice1, String choice2, String choice3, String choice4) {

        if(LastQuestionWasType1){
            // If the Last question was type1, we need to change the visisbility of the components
            HideType1Stuff();
            UnHideType0Stuff();
            LastQuestionWasType1=false;
        }

        QuestionString.setText(question);
        Choice1.setText(choice1);
        Choice2.setText(choice2);
        Choice3.setText(choice3);
        Choice4.setText(choice4);
    }

    @Override
    public void onBackPressed() {
        if(shouldallowBack) super.onBackPressed();
        else Log.i("QuestionDisplayActivity","Back is pressed");
    }

    @Override
    public void updateTime(String time) {
        remainingTime.setText(time);
    }

    @Override
    public void alertUser() { remainingTime.setBackgroundResource(R.drawable.timebackground_red); }

    @Override
    public void updatetype1question(String qnumber,String question,String choice1,String choice2,String choice3,String choice4) {

        //Loading Images
        LoadImages(qnumber, question, choice1, choice2, choice3, choice4);

        // Clearing the checked radio buttons
        choice1_rdbtn.setChecked(false);
        choice2_rdbtn.setChecked(false);
        choice3_rdbtn.setChecked(false);
        choice4_rdbtn.setChecked(false);

        // null selected
        questionPresenter.setChoice(-1);

        if(!LastQuestionWasType1){
            UnHideType1Stuff();
            HideType0Stuff();
            LastQuestionWasType1=true;
        }


    }

    void HideType1Stuff(){
        Questionnumber.setVisibility(View.INVISIBLE);
        choice1_lyt.setVisibility(View.INVISIBLE);
        choice2_lyt.setVisibility(View.INVISIBLE);
        choice3_lyt.setVisibility(View.INVISIBLE);
        choice4_lyt.setVisibility(View.INVISIBLE);

        question_img.setVisibility(View.INVISIBLE);
        choice1_img.setVisibility(View.INVISIBLE);
        choice2_img.setVisibility(View.INVISIBLE);
        choice3_img.setVisibility(View.INVISIBLE);
        choice4_img.setVisibility(View.INVISIBLE);

        choice1_rdbtn.setVisibility(View.INVISIBLE);
        choice2_rdbtn.setVisibility(View.INVISIBLE);
        choice3_rdbtn.setVisibility(View.INVISIBLE);
        choice4_rdbtn.setVisibility(View.INVISIBLE);

    }

    public void HideType0Stuff(){
        QuestionString.setVisibility(View.INVISIBLE);
        Choices.setVisibility(View.INVISIBLE);
    }

    public void UnHideType1Stuff(){
        Questionnumber.setVisibility(View.VISIBLE);

        // making images visible
        choice1_lyt.setVisibility(View.VISIBLE);
        choice2_lyt.setVisibility(View.VISIBLE);
        choice3_lyt.setVisibility(View.VISIBLE);
        choice4_lyt.setVisibility(View.VISIBLE);

        question_img.setVisibility(View.VISIBLE);
        choice1_img.setVisibility(View.VISIBLE);
        choice2_img.setVisibility(View.VISIBLE);
        choice3_img.setVisibility(View.VISIBLE);
        choice4_img.setVisibility(View.VISIBLE);

        choice1_rdbtn.setVisibility(View.VISIBLE);
        choice2_rdbtn.setVisibility(View.VISIBLE);
        choice3_rdbtn.setVisibility(View.VISIBLE);
        choice4_rdbtn.setVisibility(View.VISIBLE);

    }

    public void UnHideType0Stuff(){
        QuestionString.setVisibility(View.VISIBLE);
        Choices.setVisibility(View.VISIBLE);
    }

    private void LoadImages(String qnumber,String question,String choice1,String choice2,String choice3,String choice4) {
        Glide.with(this).load(question).placeholder(R.drawable.timebackground).error(R.drawable.timebackground_red).into(question_img);
        Glide.with(this).load(choice1).placeholder(R.drawable.timebackground).error(R.drawable.timebackground_red).into(choice1_img);
        Glide.with(this).load(choice2).placeholder(R.drawable.timebackground).error(R.drawable.timebackground_red).into(choice2_img);
        Glide.with(this).load(choice3).placeholder(R.drawable.timebackground).error(R.drawable.timebackground_red).into(choice3_img);
        Glide.with(this).load(choice4).placeholder(R.drawable.timebackground).error(R.drawable.timebackground_red).into(choice4_img);

        Questionnumber.setText(qnumber);
    }

    private void init_type1stuff(){
        //type1
        choice1_lyt = findViewById(R.id.choice1layout);
        choice2_lyt = findViewById(R.id.choice2_layout);
        choice3_lyt = findViewById(R.id.choice3_layout);
        choice4_lyt = findViewById(R.id.choice4_layout);

        question_img =  findViewById(R.id.questionimage);
        choice1_img = findViewById(R.id.choice1_image);
        choice2_img = findViewById(R.id.choice2_image);
        choice3_img = findViewById(R.id.choice3_image);
        choice4_img = findViewById(R.id.choice4_image);

        choice1_rdbtn = findViewById(R.id.choice1_radio);
        choice2_rdbtn = findViewById(R.id.choice2_radio);
        choice3_rdbtn = findViewById(R.id.choice3_radio);
        choice4_rdbtn = findViewById(R.id.choice4_radio);

    }

    private void init_Type0stuff(){
        // Text Views
        remainingTimeInfo = findViewById(R.id.remainingtimeinfo);
        remainingTime = findViewById(R.id.remainingtime);
        QuestionString = findViewById(R.id.question_string);
        loginString = findViewById(R.id.loginstring);
        subjectName = findViewById(R.id.subject);
        Questionnumber = findViewById(R.id.textView);


        // Radio buttons
        Choices = findViewById(R.id.choices_radiogroup);
        Choice1 = findViewById(R.id.choice1_radioButton);
        Choice2 = findViewById(R.id.choice2_radioButton);
        Choice3 = findViewById(R.id.choice3_radioButton);
        Choice4 = findViewById(R.id.choice4_radioButton);

        // Buttons
        nextQuestion = findViewById(R.id.nextquestion_button);
    }

    private void init_listenersoftype0(){
        Choices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice1_radioButton:
                        questionPresenter.setChoice(1);
                        break;
                    case R.id.choice2_radioButton:
                        questionPresenter.setChoice(2);
                        break;
                    case R.id.choice3_radioButton:
                        questionPresenter.setChoice(3);
                        break;
                    case R.id.choice4_radioButton:
                        questionPresenter.setChoice(4);
                        break;
                    default:
                        questionPresenter.setChoice(-1);
                        break;
                }
            }
        });
    }

    private void init_listenersoftype1(){
        choice1_rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPresenter.setChoice(1);
                choice2_rdbtn.setChecked(false);
                choice3_rdbtn.setChecked(false);
                choice4_rdbtn.setChecked(false);
            }
        });
        choice2_rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPresenter.setChoice(2);
                choice1_rdbtn.setChecked(false);
                choice3_rdbtn.setChecked(false);
                choice4_rdbtn.setChecked(false);
            }
        });

        choice3_rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPresenter.setChoice(3);
                choice2_rdbtn.setChecked(false);
                choice1_rdbtn.setChecked(false);
                choice4_rdbtn.setChecked(false);
            }
        });

        choice4_rdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPresenter.setChoice(4);
                choice2_rdbtn.setChecked(false);
                choice1_rdbtn.setChecked(false);
                choice3_rdbtn.setChecked(false);
            }
        });
    }

    public void showUpdatingMessage(){
        Questionnumber.setVisibility(View.GONE);
        choice1_lyt.setVisibility(View.GONE);
        choice2_lyt.setVisibility(View.GONE);
        choice3_lyt.setVisibility(View.GONE);
        choice4_lyt.setVisibility(View.GONE);

        question_img.setVisibility(View.GONE);
        choice1_img.setVisibility(View.GONE);
        choice2_img.setVisibility(View.GONE);
        choice3_img.setVisibility(View.GONE);
        choice4_img.setVisibility(View.GONE);

        choice1_rdbtn.setVisibility(View.GONE);
        choice2_rdbtn.setVisibility(View.GONE);
        choice3_rdbtn.setVisibility(View.GONE);
        choice4_rdbtn.setVisibility(View.GONE);

        Choices.setVisibility(View.GONE);

        loadinganimation.playAnimation();
        loadinganimation.setVisibility(View.VISIBLE);

        nextQuestion.setVisibility(View.INVISIBLE);
        QuestionString.setVisibility(View.VISIBLE);
        QuestionString.setText("Evaluating Your Answers .Please Wait...");
    }

    @Override
    public void showMarks(int marks){
        loadinganimation.pauseAnimation();
        loadinganimation.setAnimation(R.raw.successanimation2);
        loadinganimation.playAnimation();
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setText("Got it");
        QuestionString.setText("Your Mark is "+marks);
    }

    @Override
    public void ShowError(String Error) {
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.GONE);
        QuestionString.setText(Error);
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setText("Retry");
    }

    @Override
    public void ShowRetryoptions() {
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setText("Retry Updating");
        QuestionString.setText("Ubable to Update Your Marks.Press Retry");
    }

    @Override
    public void ShowRestartoptions() {
        loadinganimation.pauseAnimation();
        loadinganimation.setVisibility(View.INVISIBLE);
        QuestionString.setText("Network Error. Restart the app");
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setText("Restart");
    }

    @Override
    public void ShowWhite() {
        remainingTime.setTextColor(Color.BLACK);
        remainingTime.setBackgroundResource(R.drawable.timebackgroundwhite);
    }

    @Override
    public void ShowRed() {
        remainingTime.setTextColor(Color.WHITE);
        remainingTime.setBackgroundResource(R.drawable.timebackground_red);
    }


}
