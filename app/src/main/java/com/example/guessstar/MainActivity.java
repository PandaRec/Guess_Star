package com.example.guessstar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private String url;

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    private ImageView imageViewStart;

    private ArrayList<String> names;
    private ArrayList<String> images;
    private ArrayList<Button> buttons;

    private int numberOfQuestion;
    private int numberOfRightAnswer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = "http://dratyti.info/znamenitosti/zvyozdy-chi-nastoyashchie-imena-vas-udivyat.html";
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        imageViewStart = findViewById(R.id.imageViewStar);
        names = new ArrayList<>();
        images = new ArrayList<>();
        buttons = new ArrayList<>();
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);

        getContent();
        playGame();

    }
    private void getContent(){
        DownloadContentTask downloadContentTask = new DownloadContentTask();
        try{

            String content = downloadContentTask.execute(url).get();
            String start ="<div class=\"entry-content\" itemprop=\"articleBody\">";
            String end = "<div class=\"entry-content\" itemprop=\"articleBody\"(.*)<\\/article><!-- #post-## -->";
            String splitContent=null;
            Log.i("match_content",content);

            Pattern pattern = Pattern.compile(end);
            Matcher matcher = pattern.matcher(content);
            while(matcher.find()){
                splitContent = matcher.group(1);
                Log.i("match_content",matcher.group(1));
            }

            Pattern pattern1 = Pattern.compile("<h3>(?!<).*?>");
            Matcher matcher1 = pattern1.matcher(splitContent);
            //Log.i("mmm_content",content);
            while (matcher1.find()){
                Log.i("test_count","-");
                Log.i("match_names",matcher1.group());
                String name = matcher1.group();
                name = name.replaceAll("<h3>|</h3>","");
                //System.out.println(content);

                names.add(name);

            }

            Pattern patternImages = Pattern.compile("<h3><img(.*?)\\/>|<p><img(.*?)\\/>");
            Pattern patternSrc = Pattern.compile("src=\"(.*?)\"");
            Matcher matcherImages = patternImages.matcher(content);
            while (matcherImages.find()){

                //String midRez = matcherImages.group();
                Matcher matcherSrc = patternSrc.matcher(matcherImages.group(0));
                //Log.i("match_images",matcherImages.group(0));
                while (matcherSrc.find()){
                    Log.i("match_src",matcherSrc.group(1));
                    images.add(matcherSrc.group(1));
                }
            }
            //Log.i("size_arr",String.valueOf(images.size()));
            System.out.println("------------------>"+images.size());
            System.out.println("------------------>"+names.size());

            for(String img:images){
                Log.i("from_array",img);
            }
            for(String name:names){
                Log.i("from_names",name);
            }
            Log.i("size",String.valueOf(names.size()));
            Log.i("size",String.valueOf(images.size()));






        }catch (Exception e){
            Log.i("my_exception",e.toString());
        }
    }

    private void playGame(){
        generateQuestion();
        Log.i("question_right_name",names.get(numberOfRightAnswer));
        Log.i("question_right_img",images.get(numberOfQuestion));

        DownloadImageTask downloadImageTask = new DownloadImageTask();
        try{
            Bitmap bitmap = downloadImageTask.execute(images.get(numberOfQuestion)).get();
            if(bitmap!=null)
                imageViewStart.setImageBitmap(bitmap);
            Log.i("my","null");
            for(int i =0;i<buttons.size();i++){
                if(i==numberOfRightAnswer){
                    Log.i("button_right_name",names.get(numberOfQuestion));
                    buttons.get(i).setText(names.get(numberOfQuestion));
                }else {
                    int wrongAnswer = generateWrongAnswer();
                    Log.i("button_wrong_name",names.get(wrongAnswer));
                    buttons.get(i).setText(names.get(wrongAnswer));
                }
            }

        }catch (Exception e){
            Log.i("my_exception",e.toString());

        }

    }
    private void generateQuestion(){
        numberOfQuestion = (int) (Math.random()*names.size());
        numberOfRightAnswer = (int) (Math.random()*buttons.size());

    }

    private int generateWrongAnswer(){
        return (int)(Math.random()*names.size());
    }

    public void onClickAnswer(View view) {
        Button button = (Button) view;
        String tag = button.getTag().toString();
        Log.i("tag",tag);
        Log.i("right_answer",String.valueOf(numberOfRightAnswer));

        if(Integer.parseInt(tag)==numberOfRightAnswer){
            Toast.makeText(this, "Правильно - "+names.get(numberOfQuestion), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Неправильно - "+names.get(numberOfQuestion), Toast.LENGTH_LONG).show();
        }
        playGame();

    }
}