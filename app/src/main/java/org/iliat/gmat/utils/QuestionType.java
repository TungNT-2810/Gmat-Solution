package org.iliat.gmat.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.iliat.gmat.network.APIUrls;
import org.iliat.gmat.network.JSONQuestionList;
import org.iliat.gmat.network.JSONQuestionTypeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MrBom on 5/16/2016.
 */
public class QuestionType {
    public static JSONQuestionTypeList getQuestionType(){
        try {
            HttpURLConnection url = (HttpURLConnection) (new URL(APIUrls.QUESTION_TYPE_API)).openConnection();
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
                JSONQuestionTypeList result = (new Gson()).fromJson(reader, JSONQuestionTypeList.class);
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
