package com.fitgym.ui;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;


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
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;


public class CalendarioRutinaActivity extends AppCompatActivity {

    public static MCalendarView dlg;
    private TextView yearV;
    private TextView monthV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_rutina);

        yearV = (TextView) findViewById(R.id.anho);
        monthV = (TextView) findViewById(R.id.mes);

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        yearV.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthV.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));

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

    dlg.setOnMonthChangeListener(new OnMonthChangeListener() {
        @Override
        public void onMonthChange(int y, int m) {
            yearV.setText(String.valueOf(y));
            monthV.setText(String.valueOf(m));
        }
    });
       /* dlg.markDate(
                new DateData(new GregorianCalendar().get(Calendar.YEAR)  - 1900, new GregorianCalendar().get(Calendar.MONTH) ,new GregorianCalendar().get(Calendar.DAY_OF_MONTH)).setMarkStyle(new MarkStyle(MarkStyle.LEFTSIDEBAR, Color.BLUE)));*/

    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );
        this.getMenuInflater().inflate( R.menu.activity_actions, menu );
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean toret = false;
        switch( menuItem.getItemId() ) {
            case R.id.action_atras:
                this.finish();
                break;
        }
        return toret;
    }
    private DBManager dbManager;

}
