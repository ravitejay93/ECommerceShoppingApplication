package com.example.ravi.shopping;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by ravi on 2/5/2017.
 */

public abstract class mysql_task extends AsyncTask<String, Void, String> implements get_user_information{

    Context context;
    AlertDialog alertDialog;
    String task;
    mysql_task(Context ctx){
        context = ctx;
    }
    public abstract void onResponseReceived(String result);

    @Override
    protected String doInBackground(String... params) {
        task = params[0];
        if(params[0] == "Login" ){
            String url = "http://10.0.2.2/json_login.php";
            try {
                URL con = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)con.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8")+ "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    result +=line;
                    Log.e("ERROR",result);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(params[0] == "Register"){
            String url = "http://10.0.2.2/json_register.php";
            try {
                URL con = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)con.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                //Log.e("ERROR",params[8]);
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8")+ "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+ "&" + URLEncoder.encode("first_name","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8")+ "&" + URLEncoder.encode("last_name","UTF-8")+"="+URLEncoder.encode(params[4],"UTF-8")+ "&" + URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(params[5],"UTF-8")+ "&" + URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(params[6],"UTF-8") + "&" + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(params[7],"UTF-8")+ "&" + URLEncoder.encode("zip","UTF-8")+"="+URLEncoder.encode(params[8],"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    result +=line;
                    Log.e("ERROR",result);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login status");

    }
    @Override
    protected void onPostExecute(String result){

        if(result == null) {
            if(task == "Login"){
                result = "Please Register";
            }
            alertDialog.setMessage(result);
            alertDialog.show();
        }
        else {

            onResponseReceived(result);
        }
    }

    protected ArrayList<String> parse(String data, String name){
        ArrayList<String> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            Log.e("Error", String.valueOf(jsonObject.get("success")));
            if((int)jsonObject.get("success") == 1) {

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    result.add((String) jsonObject.get(name));

                }
                //Log.e("Error",result.get(0));
                return result;
            }
            else{
                result.add("None");
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result.add("None");
        return result;
    }
}