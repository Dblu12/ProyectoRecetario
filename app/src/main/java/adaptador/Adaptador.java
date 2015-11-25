package adaptador;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.recetario.R;

public class Adaptador extends CursorAdapter {
    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1 = (TextView) view.findViewById(R.id.tvReceta);
        tv1.setText(cursor.getString(1));
        tv1.setTag(cursor.getInt(0));
        TextView tv2 = (TextView) view.findViewById(R.id.tvInstruccionesItem);
        String ins = cursor.getString(2);
        ImageView iv = (ImageView) view.findViewById(R.id.ivItem);
        iv.setImageURI(Uri.parse(cursor.getString(3)));
        if(ins.length()<30){
            tv2.setText(ins);
        }else{
            tv2.setText(ins.substring(0,30)+"...");
        }

    }
}
