package com.example.sjc_oes;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

class PresenterClass implements ContractInterface.Presenter {

    private ContractInterface.View view;
    private Departments departments;
    private Courses courses;
    private DNumbers dnumbers;
    private Subjects subjects;
    private RequestQueue requestQueue;
    private int PressedTimes;
    private String DepartmentName,CourseName,SubjectName,DNumber,SubjectId,PasswordfromDB;
    Student student;



    PresenterClass(RequestQueue requestQueue, ContractInterface.View view){
        this.view = view;
        this.requestQueue = requestQueue;
        departments = new Departments(requestQueue,PresenterClass.this);
        courses = new Courses(requestQueue,PresenterClass.this);
        subjects = new Subjects(requestQueue,PresenterClass.this);
        dnumbers = new DNumbers(requestQueue,PresenterClass.this);
        PressedTimes = 0;
        getDataSet();
    }

    @Override
    public void onNextButtonPressed(int position) {
        switch (PressedTimes){
            case 0:
                // fetching courses
                ShowLoadingInfo();
                courses.setDepartmentID(departments.getDepartmentId(position));
                this.DepartmentName = departments.getDepartment(position);
                courses.DownloadCourses();
                System.out.println("Case 0");
                PressedTimes++;
                break;
            case 1:
                //fetching subjects
                ShowLoadingInfo();
                subjects.setCourseID(courses.getCourseId(position));
                this.CourseName = courses.getCourseName(position);
                subjects.DownloadSubjects();
                PressedTimes++;
                System.out.println("Case 1");
                break;
            case 2:
                ShowLoadingInfo();
                this.SubjectId = subjects.getSubjectId(position);
                this.SubjectName = subjects.getSubjectName(position);
                dnumbers.setSubjectId(this.SubjectId);
                dnumbers.DownloadDnumbers();
                PressedTimes++;
                System.out.println("Case 2");
                break;
            case 3:
                this.DNumber = dnumbers.getDNumber(position);
                this.PasswordfromDB = dnumbers.getPassword(position);
                System.out.println("===============================>Password is set");
                student = new Student(DNumber,DepartmentName,CourseName,SubjectName,SubjectId);
                view.showMessage(this.DNumber+" Enter Your Password");
                view.Hidespinner();
                System.out.println("Case 3");
                PressedTimes++;
                break;
            default:
                System.out.println("Case default");
                System.out.println("============>User Password"+view.getpasswordfromuser());
                if(view.getpasswordfromuser().compareTo(PasswordfromDB)==0){
                    view.startIntent(student);
                }else{
                    view.showMessage("Incorrect Password");
                    view.ReportPasswordError();
                }
                break;
        }
    }

    @Override
    public void onBackButtonPressed(int position) {
        switch (PressedTimes){
            case 1:
                view.showMessage("Loading..");
                view.updateArrayList(departments.getDepartmentList());
                view.showMessage("Select Your Department");
                PressedTimes--;
                break;
            case 2:
                view.showMessage("Loading..");
                view.updateArrayList(courses.getCourseList());
                view.showMessage("Select the Course");
                PressedTimes--;
                break;
            case 3:
                view.showMessage("Loading..");
                view.updateArrayList(subjects.getSubjectList());
                view.showMessage("Select the Subject");
                PressedTimes--;
                break;
            case 4:
                view.showMessage("Select D_Number");
                view.unHidespinner();
                PressedTimes--;
        }
    }

    @Override
    public void getDataSet() {
        ShowLoadingInfo();
        departments.DownloadDepartments();
    }

    @Override
    public void ShowLoadingInfo(){
        view.showMessage("Loading..");
    }

    @Override
    public void Reporterror(String error) {
        view.Showerrormsg(error);
    }

    @Override
    public void PresentDataset(ArrayList<String> strings,String Message) {
        view.updateArrayList(strings);
        view.showMessage(Message);
    }

    @Override
    public void refresh() {
        PressedTimes = 0;
        getDataSet();
    }

    @Override
    public void showNothingFoundError(String errorMessage,int pressedTimes) {
        view.showMessage(errorMessage);
        PressedTimes = pressedTimes;
        view.showNothingFoundError();
    }

    @Override
    public void showNoDepartmentFoundError() {
        view.showNoDepartmentFoundError();
    }
}
