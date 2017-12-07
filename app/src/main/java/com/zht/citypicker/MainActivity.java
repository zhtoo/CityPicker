package com.zht.citypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ParsedXmlData parsedXmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.main_text);
        Button btn = (Button) findViewById(R.id.btn);

        parsedXmlData = new ParsedXmlData();
        //解析Xml中的数据到parsedXmlData类中
        parsedXmlData.initProvinceDatas(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPicker picker = new CityPicker();
                picker.setContext(MainActivity.this);
                picker.setParsedXmlData(parsedXmlData);
                picker.show(getSupportFragmentManager());
                picker.setOnClickListener(new CityPicker.onDateSlectedListener() {
                    @Override
                    public void onDateSlected(String text) {
                        textView.setText(text);
                    }
                });

            }
        });

    }
}
