package com.example.david.recetario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import adaptador.Adaptador;
import clasestabla.GestorIngrediente;
import clasestabla.GestorIngredienteReceta;
import clasestabla.GestorReceta;
import clasestabla.Ingrediente;
import clasestabla.IngredienteReceta;
import clasestabla.Receta;
import contrato.Contrato;

public class MainActivity extends AppCompatActivity {


    private GestorIngrediente gestorIngrediente;
    private GestorIngredienteReceta gestorAmbos;
    private GestorReceta gestorReceta;
    private Adaptador a;
    private ListView lv;
    private Cursor c;
    //private ImageView imagenDialogoAnadirReceta;
    private Uri uri = null;
    private TextView tv1;
    private EditText etBusq;

    public static final int IDACTIVIDADFOTO = 1;
    public static final int REQUEST_IMAGE_GET = 2;

    /*****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lv = (ListView) findViewById(R.id.listView);
        gestorIngrediente = new GestorIngrediente(this);
        gestorReceta = new GestorReceta(this);
        gestorAmbos = new GestorIngredienteReceta(this);
        registerForContextMenu(lv);
        //imagenDialogoAnadirReceta= (ImageView) findViewById(R.id.ivReceta);
        init();
        tv1= (TextView) lv.findViewById(R.id.tvReceta);
        etBusq= (EditText) findViewById(R.id.etBusq);
    }

    @Override
    protected void onResume() {

        gestorReceta.open();
        gestorIngrediente.open();
        gestorAmbos.open();

        c = gestorReceta.getCursor();
        a = new Adaptador(this, c);
        lv.setAdapter(a);
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        gestorReceta.close();
        gestorIngrediente.close();
        gestorAmbos.close();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.aing) {
            final Ingrediente ing = new Ingrediente();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ingrediente");
            LayoutInflater inflater = LayoutInflater.from(this);
            int res = R.layout.altaingrediente;

            final View vista = inflater.inflate(R.layout.altaingrediente, null);

            alert.setView(vista);
            final EditText etNomAnIn = (EditText) vista.findViewById(R.id.etNomAnIn);
            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ing.setNombre(etNomAnIn.getText().toString());
                            gestorIngrediente.insert(ing);
                            putToast("Ingrediente añadido");
                        }
                    });
            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            alert.show();
        } else if (id == R.id.areceta) {


            //imagenDialogoAnadirReceta= (ImageView) findViewById(R.id.ivReceta);
            final Receta rec = new Receta();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Receta");
            LayoutInflater inflater = LayoutInflater.from(this);
            int res = R.layout.altareceta;

            final View vista = inflater.inflate(R.layout.altareceta, null);

            alert.setView(vista);
            final EditText etNomAnRe = (EditText) vista.findViewById(R.id.etNomAnRe);
            final EditText etInstrucciones = (EditText) vista.findViewById(R.id.etInstrucciones);
           // final ImageView ivReceta = (ImageView) vista.findViewById(R.id.ivReceta);

            LinearLayout layoutR = (LinearLayout) vista.findViewById(R.id.layoutreceta);

            final Cursor lista = gestorIngrediente.getCursor();

            int cont = 0;
            while (lista.moveToNext()) {
                System.out.println(lista.getPosition());
                Ingrediente in = gestorIngrediente.getRow(lista);

                System.out.println(in.toString());
                CheckBox chk = new CheckBox(this); // Como construir un checkbox;
                chk.setText(in.getNombre());
                ViewGroup.LayoutParams params = new
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                chk.setTag(in.getId());
                chk.setId(lista.getPosition());
                layoutR.addView(chk, params);
                //System.out.println(cont);
                //cont++;

                NumberPicker numberPicker = new NumberPicker(this);
                numberPicker.setMaxValue(1000);
                numberPicker.setMinValue(0);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setId(1000 + lista.getPosition());
                layoutR.addView(numberPicker, params);

            }

            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!etNomAnRe.getText().toString().isEmpty() && !etInstrucciones.getText().toString().isEmpty()) {
                                rec.setNombre(etNomAnRe.getText().toString());
                                rec.setInstrucciones(etInstrucciones.getText().toString());
                                if (uri != null) {
                                    rec.setFoto(uri);
                                }
                                uri=null;
                                int idreceta = (int) gestorReceta.insert(rec);

                                for (int j = 0; j < lista.getCount(); j++) {
                                    IngredienteReceta ingrec = new IngredienteReceta();
                                    CheckBox chk = (CheckBox) vista.findViewById(j);
                                    NumberPicker no = (NumberPicker) vista.findViewById(1000 + j);
                                    if (chk.isChecked()) {
                                        int tag = (int) chk.getTag();
                                        ingrec.setIdingrediente(tag);
                                        ingrec.setIdreceta(idreceta);
                                        ingrec.setCantidad(no.getValue());
                                        System.out.println(no.getValue() + " bbbbbbbbbb");
                                        gestorAmbos.insert(ingrec);
                                    }

                                }


                                putToast("Receta añadida");

                                c = gestorReceta.getCursor();
                                a = new Adaptador(MainActivity.this, c);
                                lv.setAdapter(a);
                            }

                        }
                    });
            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menucontextual, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {

        long id = item.getItemId();

        AdapterView.AdapterContextMenuInfo vistaInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posicion = vistaInfo.position;
        c = gestorReceta.getCursor();
        c.moveToPosition(posicion);

        if (id == R.id.mnborrar) {
            gestorReceta.deleteId(c.getLong(0));
            Log.v("ID", String.valueOf(c.getLong(0)));
            Cursor d = gestorAmbos.getCursor();
            while (d.moveToNext()) {
                if (d.getInt(d.getColumnIndex(Contrato.TablaRecetaIngrediente.IDRECETA)) == c.getLong(0)) {
                    IngredienteReceta i = gestorAmbos.getRow(d);
                    gestorAmbos.delete(i);
                }
            }
            c = gestorReceta.getCursor();
            a = new Adaptador(this, c);
            lv.setAdapter(a);
            return true;
        }else if(id == R.id.mneditar){

            final Receta rec = new Receta();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Receta");
            LayoutInflater inflater = LayoutInflater.from(this);
            int res = R.layout.altareceta;

            final View vista = inflater.inflate(R.layout.altareceta, null);

            alert.setView(vista);
            final EditText etNomAnRe = (EditText) vista.findViewById(R.id.etNomAnRe);
            final EditText etInstrucciones = (EditText) vista.findViewById(R.id.etInstrucciones);

            etNomAnRe.setText(c.getString(1));
            etInstrucciones.setText(c.getString(2));
            uri= Uri.parse(c.getString(3));

            ArrayList<Integer> ingredientesids= new ArrayList<>();
            ArrayList<Integer> cnti= new ArrayList<>();
            Cursor ingr= gestorAmbos.selectIngredientes(c.getInt(2));

            while(ingr.moveToNext()){
                ingredientesids.add(ingr.getInt(2));
                cnti.add(ingr.getInt(3));
            }

            LinearLayout layoutR = (LinearLayout) vista.findViewById(R.id.layoutreceta);


            final Cursor lista = gestorIngrediente.getCursor();

            int cont = 0;
            while (lista.moveToNext()) {

                Ingrediente in = gestorIngrediente.getRow(lista);

                System.out.println(in.toString());
                CheckBox chk = new CheckBox(this);
                chk.setText(in.getNombre());
                ViewGroup.LayoutParams params = new
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                chk.setTag(in.getId());
                chk.setId(lista.getPosition());


                NumberPicker numberPicker = new NumberPicker(this);
                numberPicker.setMaxValue(1000);
                numberPicker.setMinValue(0);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setId(1000 + lista.getPosition());
                layoutR.addView(chk, params);
                layoutR.addView(numberPicker, params);


                for (int i=0; i<ingredientesids.size(); i++){
                    if(lista.getInt(0)==ingredientesids.get(i)){
                        CheckBox chk2= (CheckBox) vista.findViewById(lista.getPosition());
                        chk2.setChecked(true);
                        NumberPicker numberPicker2= (NumberPicker) vista.findViewById(1000+lista.getPosition());
                        numberPicker2.setValue(cnti.get(i));
                    }
                }


            }

            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!etNomAnRe.getText().toString().isEmpty() && !etInstrucciones.getText().toString().isEmpty()) {
                                rec.setId(c.getInt(0));
                                rec.setNombre(etNomAnRe.getText().toString());
                                rec.setInstrucciones(etInstrucciones.getText().toString());
                                if (uri != null) {
                                    rec.setFoto(uri);
                                }
                                uri=null;
                                int idreceta = (int) gestorReceta.update(rec);

                                for (int j = 0; j < lista.getCount(); j++) {
                                    IngredienteReceta ingrec = new IngredienteReceta();
                                    CheckBox chk = (CheckBox) vista.findViewById(j);
                                    NumberPicker no = (NumberPicker) vista.findViewById(1000 + j);
                                    if (chk.isChecked()) {
                                        int tag = (int) chk.getTag();
                                        ingrec.setIdingrediente(tag);
                                        ingrec.setIdreceta(idreceta);
                                        ingrec.setCantidad(no.getValue());
                                        System.out.println(no.getValue() + " bbbbbbbbbb");
                                        gestorAmbos.insert(ingrec);
                                    }

                                }


                                putToast("Receta actualizada");

                                c = gestorReceta.getCursor();
                                a = new Adaptador(MainActivity.this, c);
                                lv.setAdapter(a);
                            }

                        }
                    });
            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alert.show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int pet, int res, Intent i) {

        if (res == RESULT_OK && pet == IDACTIVIDADFOTO) {

            Bitmap foto = (Bitmap) i.getExtras().get("data");
            FileOutputStream salida;
            try {
                //String nom = (String) i.getExtras().get("nombreReceta");
                GregorianCalendar nom = new GregorianCalendar();
                salida = new FileOutputStream(getFilesDir() + "/" + nom.getTime() + "A" + ".jpg");
                foto.compress(Bitmap.CompressFormat.JPEG, 90, salida);
                uri = Uri.fromFile(new File(getFilesDir() + "/" + nom.getTime() + "A" + ".jpg"));
                // String ruta= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "...";
                if (uri == null) {
                    Log.v("URI", "NULL");
                } else {
                    Log.v("URI", uri.toString());
                }
                /*
                Null pointer Error???
                 */
            } catch (FileNotFoundException e) {
            }
        } else if (pet == REQUEST_IMAGE_GET && res == RESULT_OK) {

//            Bitmap thumbnail = i.getParcelableExtra("data");
            uri = i.getData();
            String rutaImagen=uri.toString();
            Log.v("URI", uri.toString());


        }
        putToast("Imagen cambiada");
    }


    /*****************************************************/

    private void init() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                IngredienteReceta p = new IngredienteReceta();
                System.out.println(id+" bbb");
                System.out.println(position+" aaa");
                p= gestorAmbos.getRow2(id);
*/
                //System.out.println(id + " bbbb");
                Receta r = gestorReceta.getRow((int) id);
//                System.out.println(p.getIdreceta());
//                p.toString();
                ArrayList<Ingrediente> ingred = new ArrayList<Ingrediente>();
                ArrayList<Integer> cantidades = new ArrayList<Integer>();
                Cursor idsIngredientes = gestorAmbos.selectIngredientes((int) id);

                //idsIngredientes.moveToFirst();

                while (idsIngredientes.moveToNext()) {
//                    System.out.println(idsIngredientes.getInt(1) + " contador");
//                    System.out.println(idsIngredientes.getPosition());
                    Ingrediente in = new Ingrediente();
                    Cursor c = gestorIngrediente.nombresIngredientes(idsIngredientes.getInt(1));
//                    System.out.println(c.getCount() + "    longitud"); // Coge la longitud a 0 de los que dan null.
                    while (c.moveToNext()) {

                        in = gestorIngrediente.getRow(c);
//                        System.out.println(in.toString());
                    }
                    //in.setId(idsIngredientes.getInt(idsIngredientes.getColumnIndex(Contrato.TablaIngrediente._ID)));
                    //in.setNombre(idsIngredientes.getString(idsIngredientes.getColumnIndex(Contrato.TablaIngrediente.NOMBRE)));
                    ingred.add(in);
                    cantidades.add(idsIngredientes.getInt(3));
                }

                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre", r.getNombre());
                bundle.putString("foto", r.getFoto() + "");
                bundle.putString("instrucciones", r.getInstrucciones());
                bundle.putInt("totalingredientes", ingred.size()); // Recuerda recoger -1 para empezar desde 0;
                for (int cont = 0; cont < ingred.size(); cont++) {
                    bundle.putString("ingrediente" + cont, ingred.get(cont).getNombre());
                    bundle.putInt("cantidad" + cont, cantidades.get(cont));
                }
                i.putExtras(bundle);

                startActivity(i);

                //Seguir con la recogida de datos en el otro intent;
            }
        });
//        Cursor c= gestorIngrediente.getCursor();
    }

    public void putToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void hacerFoto(View v) {
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        Bundle bun = new Bundle();
        bun.putString("nombreReceta", (String) v.getTag());
        i.putExtras(bun);
        startActivityForResult(i, IDACTIVIDADFOTO);
    }

    public void abrirGaleria(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //PARA ABRIR UNA PALICACION QUE USE LA IMG
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    public void buscar(View v){
        if(etBusq.getText().toString().isEmpty()){
            c = gestorReceta.getCursor();
            a = new Adaptador(MainActivity.this, c);
            lv.setAdapter(a);
        }
        else{
            String where = Contrato.TablaReceta.NOMBRE + " LIKE ?";
            String[] parametros = new String[] { etBusq.getText().toString()+"%" };
            c=gestorReceta.getCursor(where, parametros);
            a= new Adaptador(MainActivity.this, c);
            lv.setAdapter(a);
        }
    }
}
