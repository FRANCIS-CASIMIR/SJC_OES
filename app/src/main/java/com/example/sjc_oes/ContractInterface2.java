package com.example.sjc_oes;

public interface ContractInterface2 {
    interface QuestionPresenterInterface {
       void setChoice(int choice);
       void updateFirstQuestion(int type, String q, String c1, String c2, String c3, String c4);
       void updateNextQuestion();
       void startFinalActivity();
       void Reporterror(String Error);
       void ReportUpdateError();
    }
    interface QuestionView{
        void updateQuestion_and_Choices(String question, String choice1, String choice2, String choice3, String choice4);
        void clearCheck();
        void makeToast();
        void startFeedbackActivity(Student student);
        void setStrings(String SubjectName, String Dnumber, boolean questiontype);
        void updateTime(String time);
        void alertUser();
        void updatetype1question(String qnumber, String question, String choice1, String choice2, String choice3, String choice4);
        void showUpdatingMessage();
        void showMarks(int Marks);
        void ShowError(String Error);
        void ShowRetryoptions();
        void ShowRestartoptions();
        void ShowWhite();
        void ShowRed();
    }
}
