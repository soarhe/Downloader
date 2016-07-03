package com.soarhe.downloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.soarhe.downloader.task.TaskInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacadeProxy.getInstance().init(this.getApplicationContext());
        Button click = (Button) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://dl.ops.baidu.com/appsearch_AndroidPhone_1012644b.apk";
                TaskInfo info = new TaskInfo(url);
                FacadeProxy.getInstance().start(info);
            }
        });
    }
}
