package com.mizael.motus;

import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mizael.motus.tools.Popupable;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Popupable {
    private TextView guessWord, tryCounter, scoreCounter, tryLimite, inputedWord;
    private EditText inputWord;
    private Button guess, generate;
    private Word word;
    private int counter, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initComponents();
        this.counter = 0;
        this.score = 0;

        this.guessWord.setText(
                this.word.hide());
        this.tryCounter.setText(
                new String(getResources().getString(R.string.try_counter) + " " + this.counter));
        this.scoreCounter.setText(
                new String(getResources().getString(R.string.score_counter) + " " + this.score));
        this.tryLimite.setText(
                new String(getResources().getString(R.string.try_limite) + " " + this.word.getWord().length()));


        this.guess.setOnClickListener(v -> {
            final Spannable inputSpan = inputWord.getText();
            if(inputSpan.toString().equals(word.getWord())){
                score++;
                Toast.makeText(this,
                        getResources().getString(R.string.congrats), Toast.LENGTH_SHORT).show();
                scoreCounter.setText(
                        new String(getResources().getString(R.string.score_counter) + " " + score));
                generate.performClick();
            }
            else if (counter == (word.getWord().length() - 1)){
                score = (score == 0) ? 0:(score - 1);
                Toast.makeText( this,
                        getResources().getString(R.string.shame),
                        Toast.LENGTH_SHORT).show();
                scoreCounter.setText(
                        new String(getResources().getString(R.string.score_counter) + " " + score));
                generate.performClick();

            }

            else{
                counter++;
                for(int i = 0; i < inputSpan.length(); i++){
                    char character = inputSpan.charAt(i);
                    if(word.isContent(character)){
                        // Lokona orange
                        inputSpan.setSpan(
                                new ForegroundColorSpan(getResources().getColor(R.color.orange)),
                                i,
                                i + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if(word.isSamePosition(character, i)){
                            // Lokona maitso
                            inputSpan.setSpan(
                                    new ForegroundColorSpan(getResources().getColor(R.color.green)),
                                    i,
                                    i + 1,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                tryCounter.setText(
                        new String(getResources().getString(R.string.try_counter) + " " + counter));
                inputedWord.setText(inputSpan);
                inputedWord.setVisibility(View.VISIBLE);
            }
            inputWord.setText("");
            inputWord.requestFocus();
        });




        this.generate.setOnClickListener(v -> {
            popup();

            word.generate();
            guessWord.setText(word.hide());
            inputWord.setText("");
            inputWord.requestFocus();
            inputedWord.setVisibility(View.GONE);
            counter = 0;

            tryCounter.setText(
                    new String(getResources().getString(R.string.try_counter) + " " + counter));
            tryLimite.setText(
                    new String(getResources().getString(R.string.try_limite) + " " + word.getWord().length()));

        });


    }


    public void initComponents(){
        this.guessWord = findViewById(R.id.textview_guess_word);
        this.tryCounter = findViewById(R.id.textview_try_counter);
        this.scoreCounter = findViewById(R.id.textview_score_counter);
        this.tryLimite = findViewById(R.id.textview_try_limite);
        this.inputedWord = findViewById(R.id.textview_inputed_word);

        this.inputWord = findViewById(R.id.edittext_input_word);

        this.guess = findViewById(R.id.button_guess);
        this.generate = findViewById(R.id.button_generate);

        try {
            this.word = new Word(getAssets().open("mots.txt"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void popup() {
        CustomPopup customPopup = new CustomPopup(MainActivity.this);
        customPopup.getTitle().setText(R.string.guessed_word);
        customPopup.getContent().setText(word.getWord());
        customPopup.getNegative().setVisibility(View.GONE);
        customPopup.getPositive().setBackgroundColor(getResources().getColor(R.color.blue));
        customPopup.getPositive().setOnClickListener(v1 -> customPopup.dismiss());
        customPopup.build();


    }



}