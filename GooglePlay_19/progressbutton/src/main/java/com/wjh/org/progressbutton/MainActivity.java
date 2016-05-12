package com.wjh.org.progressbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //模拟进度条设置
        ProgressButtonView pbv = (ProgressButtonView) findViewById(R.id.main_progress_btn);
        pbv.setMax(200);    //最大值
        pbv.setProgress(50);//当前值
        pbv.setText("25%"); //文本显示
    }
}
