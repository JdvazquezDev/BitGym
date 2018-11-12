package com.fitgym.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;


import com.fitgym.R;
import com.fitgym.core.ListaRutinas;


import java.util.Date;


public class CalendarioRutinaActivity extends AppCompatActivity {

    public static ListaRutinas ejericios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_rutina);

        CalendarView dlg = (CalendarView)this.findViewById(R.id.calendarView);

        this.ejericios = new ListaRutinas();
        dlg.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {

                Date fecha = new Date(year,month,dayOfMonth);

                Intent subActividad = new Intent( CalendarioRutinaActivity.this, ListaEjerciciosRutinaActivity.class );
                subActividad.putExtra( "fecha", fecha);
                CalendarioRutinaActivity.this.startActivity(subActividad);

            }
        });


    }
}
