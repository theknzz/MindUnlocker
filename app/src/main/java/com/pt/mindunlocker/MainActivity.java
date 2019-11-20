package com.pt.mindunlocker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_logged);
    }

    public void onOption(View view) {
        Toast.makeText(this, ((Button)view).getText(), Toast.LENGTH_SHORT).show();
    }

    public void onNumberSelected(View view) {
        Toast.makeText(this, ((Button)view).getText(), Toast.LENGTH_SHORT).show();
    }

    public void onGameCell(View view) {
        Toast.makeText(this, ((Button) view).getText(), Toast.LENGTH_SHORT).show();
    }
}
