package org.iliat.gmat.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.iliat.gmat.network.APIUrls;
import org.iliat.gmat.network.JSONQuestionList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hungtran on 3/13/16.
 */
public class QuestionPackAPI {
    public static JSONQuestionList getQuestion(){
        try {
            HttpURLConnection url = (HttpURLConnection) (new URL(APIUrls.QUESTIONS_API)).openConnection();
            InputStreamReader reader = new InputStreamReader(url.getInputStream(), "UTF-8");
            int responseCode = url.getResponseCode();
            BufferedReader bufferedReader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer response = new StringBuffer();
                bufferedReader = new BufferedReader(reader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                Log.e("asdasd",response.toString());
                JSONQuestionList result = (new Gson()).fromJson(reader, JSONQuestionList.class);
                reader.close();
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
