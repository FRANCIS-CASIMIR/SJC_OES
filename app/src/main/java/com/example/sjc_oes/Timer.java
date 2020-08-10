package com.example.sjc_oes;

import android.os.AsyncTask;

public class Timer extends AsyncTask<Void,Void,Void> {
    private int SLEEP_UPTO;
    private int Miniutes;
    private int Seconds;
    String Mins,Secs;
    QuestionPresenter questionPresenter;

    public Timer(QuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
        this.SLEEP_UPTO = 1000;
        this.Miniutes = 15;
        this.Seconds = 0;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (Miniutes>0){
            Miniutes--;
            Seconds=60;
            while (Seconds>0){
                Seconds--;
                publishProgress();
                try {
                    Thread.sleep(SLEEP_UPTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Mins = Miniutes<10 ? "0"+Miniutes : Miniutes+"";
        Secs = Seconds<10 ? "0"+Seconds : Seconds+"";
        if(Miniutes==5&&Seconds==0) {
            questionPresenter.AlertUser();
        }
        if(Miniutes<=0){
            questionPresenter.BlinkTime();
        }
        questionPresenter.setTime(Mins+" : "+Secs);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        questionPresenter.updateresponsestodb();

    }

    public void setSLEEP_UPTO() {
        this.SLEEP_UPTO = 0;
    }
}
