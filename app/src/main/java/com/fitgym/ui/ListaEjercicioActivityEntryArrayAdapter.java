package com.fitgym.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitgym.R;
import com.fitgym.core.Ejercicio;

import java.util.ArrayList;

public class ListaEjercicioActivityEntryArrayAdapter  extends ArrayAdapter<Ejercicio> {

    public ListaEjercicioActivityEntryArrayAdapter(Context context, ArrayList<Ejercicio> entries)
    {
        super( context, 0, entries );
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final LayoutInflater layoutInflater = LayoutInflater.from( this.getContext() );
        final Ejercicio entry = this.getItem(position);
        if ( convertView == null ) {
            convertView = layoutInflater.inflate( R.layout.lvejercicio_context_menu, null );
        }
        final TextView lblNombre= convertView.findViewById( R.id.lblNombre );
        final TextView lblDescripcion = convertView.findViewById( R.id.lblDescripcion);
        final ImageView lblImagen = convertView.findViewById(R.id.imgExercise);



        lblNombre.setText( entry.getNombre());
        lblDescripcion.setText( entry.getDescripcion());

        byte[] ExerciseImagen = entry.getImagen();
        Bitmap bitmap = BitmapFactory.decodeByteArray(ExerciseImagen, 0, ExerciseImagen.length);

        lblImagen.setImageBitmap(bitmap);

        return convertView;
    }

}
