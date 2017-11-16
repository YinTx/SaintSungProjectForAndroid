package com.saintsung.saintsungpmc.networkconnections;

import android.os.AsyncTask;

import com.saintsung.saintsungpmc.myinterface.IGetResultInService;


/**
 * Created by XLzY on 2017/8/10.
 */

public class AsyncTaskConnetion extends AsyncTask<String, Long, String> implements IGetResultInService {
    private IGetResultInService infaceNetConn;

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        //异步任务执行中
    }

    @Override
    protected void onPostExecute(String s) {
        infaceNetConn.getResultInService(s);
        super.onPostExecute(s);
    }


    @Override
    protected String doInBackground(String... strings) {
        SocketConnect socketConnect = new SocketConnect();
        return socketConnect.sendDate(strings[0], strings[1]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public void getResult(IGetResultInService myNetworkConnection) {
        if (myNetworkConnection == null) {
            infaceNetConn = null;
            return;
        } else if (myNetworkConnection instanceof IGetResultInService) {
            infaceNetConn = myNetworkConnection;
        }
    }

    @Override
    public void getResultInService(String s) {

    }
}

