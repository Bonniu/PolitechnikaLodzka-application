package com.team.szkielet;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView score_txt;
    TextView question_count_txt;
    TextView question_txt;
    Button btn_confirm_next;
    TextView time_txt;
    RadioGroup radio_group;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RelativeLayout relativeLayout;
    RadioButton radioButton4;

    //Podstawowy kolor dla rb
    private ColorStateList textColorDefaultRb;
    private List<Question> questionList;
    //ile pytan pokazalismy
    private int questionCounter;
    //ile wszystkich pytan
    private int questionTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quiz");
        score_txt = findViewById(R.id.score_txt);
        question_count_txt = findViewById(R.id.question_count_txt);
        question_txt = findViewById(R.id.question_txt);
        btn_confirm_next = findViewById(R.id.btn_confirm_next);
        time_txt = findViewById(R.id.time_txt);
        radio_group = findViewById(R.id.radio_group);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        relativeLayout= findViewById(R.id.relative_layout);
        textColorDefaultRb = radioButton.getTextColors();

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        //jak wywolujemy pierwszy raz to stworzy to tez baze
        questionList = dbHelper.getAllQuestions();
        questionTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();
        btn_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jesli nie bylo jeszcze robione
                if (!answered) {
                    if (radioButton.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()) {
                        checkAnswer();
                    } else
                        Toast.makeText(QuizActivity.this, "Choose your Answer", Toast.LENGTH_SHORT).show();
                } else
                    showNextQuestion();
            }
        });


    }

    private void checkAnswer() {
        answered = true;
        RadioButton rbSelected = findViewById(radio_group.getCheckedRadioButtonId());
        int answerNr = radio_group.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            score_txt.setText("Score:" + score);
        }

        showSolution();
    }

    private void showSolution() {
        radioButton.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);
        radioButton4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                radioButton.setTextColor(Color.GREEN);
                break;
            case 2:
                radioButton2.setTextColor(Color.GREEN);
                break;
            case 3:
                radioButton3.setTextColor(Color.GREEN);
                break;
            case 4:
                radioButton4.setTextColor(Color.GREEN);
                break;
        }
        if(questionCounter<questionTotal){
        btn_confirm_next.setText("Next");
           // btn_confirm_next.setOnClickListener(new View.OnClickListener() {
           //     @Override
           //     public void onClick(View v) {
            //        showNextQuestion();
           //     }
           // });
        }
        else{
            btn_confirm_next.setText("Finish Quiz");
            question_txt.setTextColor(Color.WHITE);
            question_txt.setText("BRAWO, DOTRWAŁES DO KONCA!");
            relativeLayout.setBackgroundColor(Color.BLACK);
            score_txt.setTextColor(Color.WHITE);
            radio_group.setVisibility(View.GONE);

            btn_confirm_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishQuiz();
                }
            });
        }

    }

    private void showNextQuestion() {
        radioButton.setTextColor(textColorDefaultRb);
        radioButton2.setTextColor(textColorDefaultRb);
        radioButton3.setTextColor(textColorDefaultRb);
        radioButton4.setTextColor(textColorDefaultRb);
        //zeby guziki nie byly wybrane
        radio_group.clearCheck();

        if (questionCounter < questionTotal) {
            currentQuestion = questionList.get(questionCounter);
            question_txt.setText(currentQuestion.getQuestion());
            radioButton.setText(currentQuestion.getOption1());
            radioButton2.setText(currentQuestion.getOption2());
            radioButton3.setText(currentQuestion.getOption3());
            radioButton4.setText(currentQuestion.getOption4());
            questionCounter++;

            question_count_txt.setText("Question: " + questionCounter + "/" + questionTotal);
            //??????
            answered = false;
            btn_confirm_next.setText("Confirm");

        } else{
            //Jak odpowiiem na wszystkie pytania
            btn_confirm_next.setText("Finish Quiz");
            question_txt.setTextColor(Color.WHITE);
            question_txt.setText("BRAWO, DOTRWAŁES DO KONCA!");
            relativeLayout.setBackgroundColor(Color.BLACK);
            score_txt.setTextColor(Color.WHITE);
            radio_group.setVisibility(View.GONE);

            btn_confirm_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishQuiz();
                }
            });
        }

    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, QuizMainActivity.class);
        startActivity(intent);
        Toast.makeText(QuizActivity.this, "Quiz Zakonczony", Toast.LENGTH_SHORT).show();


    }
}
