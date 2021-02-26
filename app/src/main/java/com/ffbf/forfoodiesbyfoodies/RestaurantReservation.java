package com.ffbf.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class RestaurantReservation extends AppCompatActivity {

    private String restaurant_name;
    private CalendarView calendar;
    private Button next_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reservation);
        getSupportActionBar().hide();

        final Intent i = getIntent();
        restaurant_name = i.getStringExtra("name");

        calendar = findViewById(R.id.make_reservation_calendar);
        calendar.setMinDate(System.currentTimeMillis()-1000);
        next_step = findViewById(R.id.make_reservation_next_step);

        final String rest_name = restaurant_name.replace(" ","%20");

        CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                month++;
                final int d = day, m=month, y=year;

                next_step.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //create link for the Opentable website
                        String url ="https://www.opentable.com/s/?covers=1&dateTime="+ y +"-"+m+"-"+d+"%2018%3A00&latitude=52.6&longitude=-2&metroId=3146&term="+rest_name+"&enableSimpleCuisines=true&includeTicketedAvailability=true&pageType=0";
                        Intent go_to_opentable = new Intent(Intent.ACTION_VIEW);
                        go_to_opentable.setData(Uri.parse(url));
                        startActivity(go_to_opentable);
                    }
                });
            }
        };
        calendar.setOnDateChangeListener(myCalendarListener);
    }
}