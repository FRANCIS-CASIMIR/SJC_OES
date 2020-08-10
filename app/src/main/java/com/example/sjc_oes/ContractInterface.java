package com.example.sjc_oes;

import java.util.ArrayList;

interface ContractInterface {
    interface Presenter{
        void onNextButtonPressed(int position);
        void onBackButtonPressed(int position);
        void getDataSet();
        void PresentDataset(ArrayList<String> strings, String Message);
        void ShowLoadingInfo();
        void Reporterror(String error);
        void Refresh(int pressedTimes);
    }
    interface View{
        void updateArrayList(ArrayList<String> arrayList);
        void showMessage(String string);
        void startIntent(Student student);
        void Hidespinner();
        void unHidespinner();
        String getpasswordfromuser();
        void Showerrormsg(String error);
        void ReportPasswordError();

    }
}
