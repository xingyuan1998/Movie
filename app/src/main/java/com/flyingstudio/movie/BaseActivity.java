package com.flyingstudio.movie;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by xingy on 2017-9-22.
 */

public class BaseActivity extends AppCompatActivity {

    public void makeToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();

    }



}
