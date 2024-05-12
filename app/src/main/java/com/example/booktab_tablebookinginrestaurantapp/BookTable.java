package com.example.booktab_tablebookinginrestaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BookTable extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Button date, time, complete;
    EditText e_r_date, e_r_time, name, phone;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String u_name, u_phone, u_e_r_date, u_e_r_time, u_selectedTable;
    String u;
    private FirebaseAuth firebaseAuth;
    NetworkInfo nInfo;
    Spinner spinner_table;
    String selectedTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_table);

        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        e_r_date= findViewById(R.id.e_r_date);
        e_r_time= findViewById(R.id.e_r_time);
        date= (Button) findViewById(R.id.date);
        time= (Button) findViewById(R.id.time);
        complete= (Button) findViewById(R.id.complete);

        firebaseAuth= FirebaseAuth.getInstance();
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        nInfo = cManager.getActiveNetworkInfo();

        spinner_table = (Spinner) findViewById(R.id.spinner);
        String[] comps = getResources().getStringArray(R.array.table);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comps);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_table.setAdapter(arrayAdapter);
        spinner_table.setOnItemSelectedListener(this);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BookTable.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                e_r_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookTable.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                e_r_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (valSpin()){
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("all");
                            UserBooking userBooking = new UserBooking(u_name, u_phone, u_e_r_date, u_e_r_time, selectedTable);
                            ref.child(ref.push().getKey()).setValue(userBooking);
                            Toast.makeText(BookTable.this, "Table Reservation Successfully Completed", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(BookTable.this, Home.class));
                            finish();
                        }
                    }
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedTable = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedTable, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Boolean validate(){
        boolean result= false;

        u_e_r_date = e_r_date.getText().toString();
        u_e_r_time= e_r_time.getText().toString();
        u_name= name.getText().toString();
        u_phone= phone.getText().toString();

        if(u_e_r_time.isEmpty() || (u_e_r_date.isEmpty() || u_name.isEmpty() || u_phone.isEmpty())){
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
        }else {
            result= true;
        }
        return result;
    }

    private Boolean valSpin(){
        boolean result= false;

        if (selectedTable.equals("Select Table for persons")) {
            Toast.makeText(this, "First select table for persons", Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;

    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        super.onBackPressed();
        startActivity(new Intent(BookTable.this, Home.class));
        finish();
    }
}