package com.zejian.activitytask_03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by zejian
 * Time 16/7/23.
 * Description:
 */
public class ActivityC extends Activity {

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        btn= (Button) findViewById(R.id.mainC);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ActivityC.this,ActivityD.class);
                startActivity(i);
            }
        });
    }

}
