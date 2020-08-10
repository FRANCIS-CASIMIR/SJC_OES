package com.example.sjc_oes;

import com.android.volley.RequestQueue;

public class QuestionPresenter implements ContractInterface2.QuestionPresenterInterface {


    private ContractInterface2.QuestionView questionView;
    private Questions questions;
    private int RunningQuestion;
    private int Choice ,NumberofError,TimeBlinker=1;
    Student student;
    Timer timer;
    private boolean TestFinished,ErrorReported;

    QuestionPresenter(RequestQueue requestQueue, ContractInterface2.QuestionView questionView,Student student){
       this.questionView = questionView;
       this.student = student;
       TestFinished = false;
       ErrorReported=false;
       RunningQuestion = 0;
       NumberofError=0;
       Choice= -1;
       questions = new Questions(requestQueue,QuestionPresenter.this);
       questions.setSubjectId(student.getSubjectId());
       timer = new Timer(this);
    }

    @Override
    public void setChoice(int choice) {
        Choice = choice;
    }
    @Override
    public void updateFirstQuestion(int type,String q, String c1, String c2, String c3, String c4) {
        // It is called by the Questions class
        if(type==0){
            questionView.setStrings(student.getSubject(),student.getDNumber(),true);
        questionView.updateQuestion_and_Choices(RunningQuestion + 1 + ") " +q, c1, c2, c3, c4);}
        else{
            questionView.setStrings(student.getSubject(),student.getDNumber(),false);
            questionView.updatetype1question(RunningQuestion + 1 + ") ",q,c1,c2,c3,c4);
        }
        timer.execute();
    }

    @Override
    public void updateNextQuestion() {
        if(!TestFinished) {
            if(ErrorReported){
                questionView.startFeedbackActivity(student);
            }
           else if (RunningQuestion < 14) {
                if (Choice != -1) {
                    questions.updateResponse(Choice);
                    RunningQuestion++;
                    if (questions.getType() == 0) {
                        questionView.updateQuestion_and_Choices(RunningQuestion + 1 + ") " + questions.getQuestionList(),
                                questions.getChoice1(),
                                questions.getChoice2(),
                                questions.getChoice3(),
                                questions.getChoice4());
                        questionView.clearCheck();
                    } else {
                        //type 1 part
                        questionView.updatetype1question(RunningQuestion + 1 + ") ", questions.getQuestionList(),
                                questions.getChoice1(),
                                questions.getChoice2(),
                                questions.getChoice3(),
                                questions.getChoice4());
                    }
                } else {
                    questionView.makeToast();
                }
            } else {
                if (Choice != -1) {
                    timer.setSLEEP_UPTO();
                } else questionView.makeToast();
            }
        }
        else{
            if(ErrorReported){
                if(NumberofError<3) {
                    // can retry 2 times.
                    retryUpdate();
                }else {
                    // Re do the test
                    questionView.startFeedbackActivity(student);
                }
            }
            else{
                questionView.startFeedbackActivity(student);
            }
        }
    }

    public void setTime(String time){
        questionView.updateTime(time);
    }
    public void AlertUser(){
        questionView.alertUser();
    }

    public void BlinkTime(){
        if(TimeBlinker%2==0){
            questionView.ShowRed();
        }else {
            questionView.ShowWhite();
        }
        TimeBlinker++;
    }

    public void updateresponsestodb(){
        TestFinished = true;
        questionView.showUpdatingMessage();
        if (Choice !=-1) questions.updateResponse(Choice);
        student.setMark(questions.getMark());
        questions.UpdateResponsestodb(student.getSubjectId(),student.getDNumber());
    }

    @Override
    public void startFinalActivity(){
        questionView.showMarks(questions.getMark());
    }

    @Override
    public void Reporterror(String Error) {
        questionView.ShowError(Error);
        ErrorReported=true;
    }

    @Override
    public void ReportUpdateError() {
        ErrorReported=true;
        NumberofError++;
        if(NumberofError<3) {
            questionView.ShowRetryoptions();
        }
        else{
            questionView.ShowRestartoptions();
        }
    }

    private void retryUpdate(){
        questionView.showUpdatingMessage();
        questions.UpdateResponsestodb(student.getSubjectId(),student.getDNumber());
    }
}

