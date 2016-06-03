//package org.iliat.gmat.dao;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import org.iliat.gmat.model.Answer;
//import org.iliat.gmat.model.QuestionPack;
//import org.iliat.gmat.model.Question;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
///**
// * Created by hungtran on 4/15/16.
// */
//public class DatabaseHelper extends SQLiteOpenHelper {
//    private static String DB_PATH = "/data/data/org.iliat.gmat/databases/";
//    private static String DB_NAME = "gmatquestion.db";
//    public static final int DB_VERSION = 1;
//    private SQLiteDatabase myDataBase;
//    private final Context myContext;
//
//    public DatabaseHelper(Context myContext){
//        super(myContext, DB_NAME, null, DB_VERSION);
//        this.myContext = myContext;
//    }
//
//    public void createDataBase() throws IOException {
//
//        boolean dbExist = checkDataBase();
//
//        if(dbExist){
//            //do nothing - database already exist
//        }else{
//            //By calling this method and empty database will be created into the default system path
//            //of your application so we are gonna be able to overwrite that database with our database.
//            this.getReadableDatabase();
//
//            try {
//
//                copyDataBase();
//
//            } catch (IOException e) {
//
//                throw new Error("Error copying database");
//
//            }
//        }
//
//    }
//
//    private void copyDataBase() throws IOException{
//        //Open your local db as the input stream
//        InputStream myInput = myContext.getAssets().open(DB_NAME);
//
//        // Path to the just created empty db
//        String outFileName = DB_PATH + DB_NAME;
//
//        //Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(outFileName);
//
//        //transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer))>0){
//            myOutput.write(buffer, 0, length);
//        }
//
//        //Close the streams
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//
//    }
//
//    private boolean checkDataBase() {
//        SQLiteDatabase checkDB = null;
//
//        try{
//            String myPath = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//
//        }catch(SQLiteException e){
//
//            //database does't exist yet.
//
//        }
//
//        if(checkDB != null){
//
//            checkDB.close();
//
//        }
//
//        return checkDB != null ? true : false;
//    }
//
//    public void openDataBase() throws SQLException {
//
//        //Open the database
//        String myPath = DB_PATH + DB_NAME;
//        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//
//    }
//
//    @Override
//    public synchronized void close() {
//
//        if(myDataBase != null)
//            myDataBase.close();
//
//        super.close();
//
//    }
//
//    public ArrayList<QuestionPack> getQuestionPackModel() throws ParseException {
//        openDataBase();
//        ArrayList<QuestionPack> arrayListQuestionPack = new ArrayList<QuestionPack>();
//        Cursor cursor = myDataBase.rawQuery("SELECT * FROM tblquestionpack", null);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        while(cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String key = cursor.getString(1);
//            String type = cursor.getString(2);
//            String strDate = cursor.getString(3);
//            Date date = dateFormat.parse(strDate);
//            Cursor cursor1 = myDataBase.rawQuery("SELECT * FROM tblquestionpack,tblquestioninpack,tblquestion WHERE tblquestioninpack.packid = tblquestionpack.id AND tblquestioninpack.questionid = tblquestion.id;", null);
//            ArrayList<Question> arrayListQuestion = new ArrayList<>();
//            while (cursor1.moveToNext()){
//                int idQuestion = cursor1.getInt(7);
//                String typeQuestion = cursor1.getString(8);
//                String stimulusQuestion = cursor1.getString(9);
//                String stemQuestion = cursor1.getString(10);
//                int rightAnswer = cursor1.getInt(11);
//                int userChoise = cursor1.getInt(12);
//                int tagQuestion = cursor1.getInt(13);
//                int starQuestion = cursor1.getInt(14);
//                Cursor cursor2 = myDataBase.rawQuery("SELECT * FROM tblanswer WHERE\n" +
//                        "\ttblanswer.questionid = " + String.valueOf(idQuestion), null);
//                ArrayList<Answer> arrayListAnswer = new ArrayList<>();
//                while (cursor2.moveToNext()){
//                    int idAnswer = cursor2.getInt(0);
//                    String contentAnswer = cursor2.getString(1);
//                    String explanationAnswer = cursor2.getString(2);
//                    int indexAnswer = cursor2.getInt(3);
//                    Answer answer = new Answer(idAnswer, contentAnswer, explanationAnswer, indexAnswer);
//                    arrayListAnswer.add(answer);
//                }
//                Question question = new Question(idQuestion, stimulusQuestion, stemQuestion
//                        , rightAnswer,userChoise, typeQuestion,arrayListAnswer, tagQuestion, starQuestion);
//                arrayListQuestion.add(question);
//            }
//            QuestionPack questionPack = new QuestionPack(id, key, type, date, arrayListQuestion);
//            arrayListQuestionPack.add(questionPack);
//        }
//        return arrayListQuestionPack;
//    }
//
//    public ArrayList<QuestionPack> getQuestionPack() throws ParseException {
//        openDataBase();
//        ArrayList<QuestionPack> arrayListQuestionPack = new ArrayList<QuestionPack>();
//        Cursor cursor = myDataBase.rawQuery("SELECT * FROM tblquestionpack", null);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        while(cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String key = cursor.getString(1);
//            String type = cursor.getString(2);
//            String strDate = cursor.getString(3);
//            Date date = dateFormat.parse(strDate);
//            Cursor cursor1 = myDataBase.rawQuery("SELECT * FROM tblquestionpack,tblquestioninpack,tblquestion WHERE tblquestioninpack.packid = tblquestionpack.id AND tblquestioninpack.questionid = tblquestion.id;", null);
//            ArrayList<Question> arrayListQuestion = new ArrayList<>();
//            while (cursor1.moveToNext()){
//                int idQuestion = cursor1.getInt(7);
//                String typeQuestion = cursor1.getString(8);
//                String stimulusQuestion = cursor1.getString(9);
//                String stemQuestion = cursor1.getString(10);
//                int rightAnswer = cursor1.getInt(11);
//                int userChoise = cursor1.getInt(12);
//                int tagQuestion = cursor1.getInt(13);
//                int starQuestion = cursor1.getInt(14);
//                Cursor cursor2 = myDataBase.rawQuery("SELECT * FROM tblanswer WHERE\n" +
//                        "\ttblanswer.questionid = " + String.valueOf(idQuestion), null);
//                ArrayList<Answer> arrayListAnswer = new ArrayList<>();
//                while (cursor2.moveToNext()){
//                    int idAnswer = cursor2.getInt(0);
//                    String contentAnswer = cursor2.getString(1);
//                    String explanationAnswer = cursor2.getString(2);
//                    int indexAnswer = cursor2.getInt(3);
//                    Answer answer = new Answer(idAnswer, contentAnswer, explanationAnswer, indexAnswer);
//                    arrayListAnswer.add(answer);
//                }
//                Question question = new Question(idQuestion, stimulusQuestion, stemQuestion
//                        , rightAnswer,userChoise, typeQuestion,arrayListAnswer, tagQuestion, starQuestion);
//                arrayListQuestion.add(question);
//            }
//            QuestionPack questionPack = new QuestionPack(id, key, type, date, arrayListQuestion);
//            arrayListQuestionPack.add(questionPack);
//        }
//        return arrayListQuestionPack;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//}
