package com.fitgym.ui;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;


import com.fitgym.R;
import com.fitgym.core.DBManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;


public class CalendarioRutinaActivity extends AppCompatActivity {

    private MCalendarView dlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_rutina);

        //CalendarView c = (CalendarView)this.findViewById(R.id.calendarView);

        dlg = ((MCalendarView) findViewById(R.id.calendarView));
        dlg.hasTitle(false);

        dlg.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(new Date(date.getYear() - 1900,date.getMonth() - 1,date.getDay()));

                Intent subActividad = new Intent( CalendarioRutinaActivity.this, ListaEjerciciosRutinaActivity.class );
                subActividad.putExtra( "fecha", selectedDate);
                CalendarioRutinaActivity.this.startActivity(subActividad);
            }
        });
    }

    protected void onResume() {

        super.onResume();

        this.dbManager = DBManager.get();
        Cursor c = this.dbManager.getAllDatesRutina();

        if (c.moveToFirst()) {
            do {
                String aux = c.getString(c.getColumnIndex("fecha"));

                String[] aux2 = aux.split("-");
                dlg.markDate(
                        new DateData(Integer.parseInt(aux2[0]),Integer.parseInt(aux2[1]),Integer.parseInt(aux2[2])).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)));

            } while (c.moveToNext());
        }
    //    Log.i("calendar", String.valueOf(new GregorianCalendar().get(Calendar.YEAR)));
    //    Log.i("calendar", String.valueOf(new GregorianCalendar().get(Calendar.MONTH)));
    //    Log.i("calendar", String.valueOf(new GregorianCalendar().get(Calendar.DAY_OF_MONTH)));

        dlg.markDate(
                new DateData(new GregorianCalendar().get(Calendar.YEAR)  - 1900, new GregorianCalendar().get(Calendar.MONTH) ,new GregorianCalendar().get(Calendar.DAY_OF_MONTH)).setMarkStyle(new MarkStyle(MarkStyle.LEFTSIDEBAR, Color.BLUE)));

    }

    private DBManager dbManager;

}
