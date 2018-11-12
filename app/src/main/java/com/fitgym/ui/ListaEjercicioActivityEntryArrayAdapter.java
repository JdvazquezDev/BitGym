package com.fitgym.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fitgym.R;
import com.fitgym.core.Ejercicio;

public class ListaEjercicioActivityEntryArrayAdapter  extends ArrayAdapter<Ejercicio> {

    public ListaEjercicioActivityEntryArrayAdapter(Context context, Ejercicio[] entries)
    {
        super( context, 0, entries );
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final LayoutInflater layoutInflater = LayoutInflater.from( this.getContext() );
        final Ejercicio entry = this.getItem( position );
        if ( convertView == null ) {
            convertView = layoutInflater.inflate( R.layout.lista_ejercicios, null );
        }
        final TextView lblNombre= convertView.findViewById( R.id.lblNombre );
        final TextView lblDescripcion = convertView.findViewById( R.id.lblDescripcion);
        lblNombre.setText( entry.getNombre());
        lblDescripcion.setText( entry.getDescripcion());
        return convertView;
    }

}
