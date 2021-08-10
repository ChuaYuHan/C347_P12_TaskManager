package com.myapplicationdev.android.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    int reqCode = 12345;

    EditText etName, etDesc, etRemind;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDes);
        etRemind = findViewById(R.id.etRemind);
        btnAdd = findViewById(R.id.btnAddTask);
        btnCancel = findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                int remind = Integer.parseInt(etRemind.getText().toString());

                DBHelper db = new DBHelper(AddActivity.this);
                db.insertTask(name, desc);
                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                Log.i("info", "Task: " + name + " Desc: " + desc);
                etName.setText("");
                etDesc.setText("");
                etRemind.setText("");
                db.close();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, remind);

                Intent intent = new Intent(AddActivity.this, MyReceiver.class);
                Task task = new Task(name, desc);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                intent.putExtra("bundle", bundle);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}