package th.ac.su.cp.quizgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import th.ac.su.cp.quizgame.model.WordItem;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mQuestionImageView;
    private Button[] mButtons = new Button[4];
    int count_score = 0;
    int wrong = 0;

    private String mAnswerWord;
    private Random  mRandom;
    private List<WordItem> mItemList;
    //private Button mChoice1Button,mChoice2Button,mChoice3Button,mChoice4Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mItemList = new ArrayList<>(Arrays.asList(WordListActivity.items));
        mQuestionImageView = findViewById(R.id.question_image_view);
        mButtons[0] = findViewById(R.id.choice_1_button);
        mButtons[1] = findViewById(R.id.choice_2_button);
        mButtons[2] = findViewById(R.id.choice_3_button);
        mButtons[3] = findViewById(R.id.choice_4_button);

        mButtons[0].setOnClickListener(this);
        mButtons[1].setOnClickListener(this);
        mButtons[2].setOnClickListener(this);
        mButtons[3].setOnClickListener(this);

        mRandom  = new Random();
        newQuiz();
    }

    private void newQuiz() {
        List<WordItem> mItemList = new ArrayList<>(Arrays.asList(WordListActivity.items));
        //สุ่ม Index ของคำศัพท์
        int answerIndex = mRandom.nextInt(mItemList.size());
        //เข้าถึง WordItem ตาม Index ที่สุ่มได้
        WordItem item = mItemList.get(answerIndex);
        // แสดงรูปคำถาม
        mQuestionImageView.setImageResource(item.imageResId);

        mAnswerWord = item.word;

        //สุ่มตำแหน่งที่จะแสดงคำตอบ
        int randomButton = mRandom.nextInt(4);
        //แสดงคำศัพท์ที่เป็นคำตอบ
        mButtons[randomButton].setText(item.word);
        //ลบ WordItem ที่เป็นคำตอบออกจาก
        mItemList.remove(item);

        //เอา List ที่เหลือมา shuffle
        Collections.shuffle(mItemList);
//--------------------------------------------------------------------------------------------------
        //เอาข้อมูลจาก List แสดงที่ปุ่ม 3 ปุ่มที่ไม่ใช่คำตอบ
        for(int i=0; i<4; i++){
            if(i == randomButton){
                continue;
            }
            mButtons[i].setText(mItemList.get(i).word);
        }
    }

    @Override
    public void onClick(View view) {
        final TextView scoreView = findViewById(R.id.score_point_view);
        Button b = findViewById(view.getId());
        final String point;
        String buttonText = b.getText().toString();
            if (buttonText.equals(mAnswerWord)) {
                count_score++;
                point = String.valueOf(count_score);
                scoreView.setText(point + " คะแนน");
            } else {
                wrong++;
            }

            if(count_score+wrong < 5) {
                newQuiz();
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
                final Intent i = new Intent(GameActivity.this, MainActivity.class);
                    dialog.setTitle("สรุปผล");
                    dialog.setMessage("คุณได้ "+count_score+" คะแนน\n\nต้องดารเล่นเหมใหม่หรือไม่");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            newQuiz();
                            scoreView.setText("");
                            count_score=0;
                            wrong=0;
                        }
                    }
                    );
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    startActivity(i);
                                }
                            }
                    );
                    dialog.show();

            }
    }


}
