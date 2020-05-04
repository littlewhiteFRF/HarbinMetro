package com.frf.harbinmetro.contact;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.contact.model.QuestionInfo;

public class QuestionInfoActivity extends AppCompatActivity {

    private Toolbar question_info_activity_toolbar;//顶部导航栏

    private TextView question;
    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_info);

        question_info_activity_toolbar = findViewById(R.id.question_info_activity_toolbar);
        question_info_activity_toolbar.setTitle("问题解答");
        question_info_activity_toolbar.setLogo(R.drawable.ic_action_contact_assistant);
        setSupportActionBar(question_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        QuestionInfo questionInfo = new QuestionInfo();
        questionInfo = (QuestionInfo) getIntent().getSerializableExtra("question");

        question = findViewById(R.id.question_info_activity_question);
        answer = findViewById(R.id.question_info_activity_answer);

        question.setText(questionInfo.getQuestionTitle().toString());
        answer.setText(questionInfo.getAnswer().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://ToolBar最左侧的按钮叫做HomeAsUp按钮，而且id永远是android.R.id.home
                finish();//结束这个这个活动
                break;
            default:
                break;
        }
        return true;
    }
}
