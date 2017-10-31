package com.saintsung.saintsungpmc.networkconnections;

import android.os.AsyncTask;

/**
 * Created by XLzY on 2017/8/10.
 */

public class AsyncTaskConnetion extends AsyncTask<String,Long,String> {

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        //异步任务执行中
    }

    @Override
    protected void onPostExecute(String s) {


        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
