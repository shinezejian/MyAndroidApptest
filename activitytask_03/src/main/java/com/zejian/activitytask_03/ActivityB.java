package com.zejian.activitytask_03;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by zejian
 * Time 16/7/23.
 * Description:
 */
public class ActivityB extends Activity {
    private Button btn;
    private Button btnC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        btn= (Button) findViewById(R.id.main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.zejian.activitytask04", "com.zejian.activitytask04.ActivityD");
                intent.setComponent(cn);
                startActivity(intent);

            }
        });


//        btnC= (Button) findViewById(R.id.mainD);
//        btnC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                ComponentName cn = new ComponentName("com.cmcm.activitytask2", "com.cmcm.activitytask2.ActivityC");
//                intent.setComponent(cn);
//                startActivity(intent);
//
//            }
//        });


       
    }
}
