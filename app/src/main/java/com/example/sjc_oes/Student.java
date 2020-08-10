package com.example.sjc_oes;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private String DNumber;
    private String Department;
    private String Course;
    private String Subject;
    private int Mark;
    private String SubjectId;

    // in the first activity , we will get all the above data except the mark, so i construct the 'Mark' field as o


    public Student(String DNumber, String department, String course, String subject, String subjectId) {
        this.DNumber = DNumber;
        Department = department;
        Course = course;
        Subject = subject;
        SubjectId = subjectId;
        Mark = 0;// default value
    }

    /*
    1)all the values are set in the constructor except Mark and used throughout the completion of a particular test
     So we need setter only for Mark.

    2) We use the SubjectName and Dnumber in the QuestionDisplayActivity and
     Mark in the FeedBackActivity So we need getter for DNumber, Subject and Mark

    3) We Use SubjectId to Update the response into the database, so we have to have getter for Subjectid

     */


    public void setMark(int mark) {
        Mark = mark;
    }

    public String getDNumber() {
        return DNumber;
    }

    public String getSubject() {
        return Subject;
    }

    public int getMark() {
        return Mark;
    }

    public String getSubjectId() {
        return SubjectId;
    }

    // Parcelable Implementation part

    protected Student(Parcel in) {
        DNumber = in.readString();
        Department = in.readString();
        Course = in.readString();
        Subject = in.readString();
        Mark = in.readInt();
        SubjectId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DNumber);
        dest.writeString(Department);
        dest.writeString(Course);
        dest.writeString(Subject);
        dest.writeInt(Mark);
        dest.writeString(SubjectId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

}
