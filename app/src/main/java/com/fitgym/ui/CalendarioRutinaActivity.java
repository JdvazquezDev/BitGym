package com.fitgym.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;


import com.fitgym.R;
import com.fitgym.core.DBManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CalendarioRutinaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_rutina);

        this.dbManager = DBManager.get();

        CalendarView dlg = (CalendarView)this.findViewById(R.id.calendarView);

        dlg.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = sdf.format(new Date(year - 1900,month,dayOfMonth));

                Intent subActividad = new Intent( CalendarioRutinaActivity.this, ListaEjerciciosRutinaActivity.class );
                subActividad.putExtra( "fecha", selectedDate);
                CalendarioRutinaActivity.this.startActivity(subActividad);

            }
        });


    }


    private DBManager dbManager;

}
