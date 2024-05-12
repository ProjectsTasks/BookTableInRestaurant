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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateOpen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String getKey;

    private EditText name_up, phone_up;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Button update;
    TextView e_r_date_up, e_r_time_up;
    NetworkInfo nInfo;
    private FirebaseDatabase firebaseDatabase;

    Button date, time, complete;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String u_name, u_phone, u_e_r_date, u_e_r_time, u_selectedTable;
    Spinner spinner_table;
    String selectedTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_open);

        Intent intent = getIntent();
        getKey = intent.getStringExtra("key");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        name_up = findViewById(R.id.name_up);
        phone_up = findViewById(R.id.phone_up);
        e_r_date_up = findViewById(R.id.e_r_date_up);
        e_r_time_up = findViewById(R.id.e_r_time_up);
        date = findViewById(R.id.c_date);
        time = findViewById(R.id.c_time);
        complete = findViewById(R.id.u_complete);

        spinner_table = (Spinner) findViewById(R.id.spinner1);
        String[] comps = getResources().getStringArray(R.array.table1);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comps);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_table.setAdapter(arrayAdapter);
        spinner_table.setOnItemSelectedListener(this);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        nInfo = cManager.getActiveNetworkInfo();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //get firebase user
        user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("all").child(getKey);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserBooking userBooking = dataSnapshot.getValue(UserBooking.class);
                name_up.setText(userBooking.getBookName());
                phone_up.setText(userBooking.getBookPhone());
                e_r_date_up.setText(userBooking.getBookDate());
                e_r_time_up.setText(userBooking.getBookTime());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateOpen.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateOpen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                e_r_date_up.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateOpen.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                e_r_time_up.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (valSpin()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("all").child(getKey);
                            UserBooking userBooking = new UserBooking(u_name, u_phone, u_e_r_date, u_e_r_time, selectedTable);
                            ref.setValue(userBooking);
                            Toast.makeText(UpdateOpen.this, "Table Reservation Updated Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UpdateOpen.this, Home.class));
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

            u_e_r_date = e_r_date_up.getText().toString();
            u_e_r_time= e_r_time_up.getText().toString();
            u_name= name_up.getText().toString();
            u_phone= phone_up.getText().toString();

            if(u_e_r_time.isEmpty() || (u_e_r_date.isEmpty() || u_name.isEmpty() || u_phone.isEmpty())){
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
            }else {
                result= true;
            }
            return result;
        }

        private Boolean valSpin(){
            boolean result= false;

            if (selectedTable.equals("Change Table for persons")) {
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
            startActivity(new Intent(UpdateOpen.this, Home.class));
            finish();
    }
}