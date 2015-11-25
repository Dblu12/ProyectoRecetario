package clasestabla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import contrato.Ayudante;
import contrato.Contrato;

/**
 * Created by David on 18/11/2015.
 */
public class GestorReceta {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorReceta(Context c) {
        abd = new Ayudante(c);
    }
    public void open() {
        bd = abd.getWritableDatabase();
    }
    public void openRead() {
        bd = abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    public long insert(Receta ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaReceta.NOMBRE,
                ag.getNombre());
        valores.put(Contrato.TablaReceta.INSTRUCCIONES,
                ag.getInstrucciones());
        valores.put(Contrato.TablaReceta.FOTO,
                String.valueOf(ag.getFoto()));
        long id = bd.insert(Contrato.TablaReceta.TABLA,
                null, valores);
        return id;

        // Devuelve -1 si falla.
    }



    public int delete(Receta ag) {

        return deleteId(ag.getId());
    }

    public int deleteId(long id) {
        String condicion = Contrato.TablaReceta._ID + " = ?";
        String[] argumentos = { id + "" };
        int cuenta = bd.delete(
                Contrato.TablaReceta.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Receta ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaReceta.NOMBRE, ag.getNombre());
        valores.put(Contrato.TablaReceta.INSTRUCCIONES, ag.getInstrucciones());
        valores.put(Contrato.TablaReceta.FOTO, String.valueOf(ag.getFoto()));
        String condicion = Contrato.TablaReceta._ID + " = ?";
        String[] argumentos = { ag.getId() + "" };
        int cuenta = bd.update(Contrato.TablaReceta.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    public List<Receta> select(String condicion) {
        List<Receta> la = new ArrayList<Receta>();
        Cursor cursor = bd.query(Contrato.TablaReceta.TABLA, null,
                condicion, null, null, null, null);
        cursor.moveToFirst();
        Receta ag;
        while (!cursor.isAfterLast()) {
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }

    public List<Receta> select2(String condicion, String[] params){
        List<Receta> la= new ArrayList<>();
        Cursor cursor = bd.query(Contrato.TablaReceta.TABLA, null, condicion, params, null, null, null);
        cursor.moveToFirst();
        Receta ag;
        while(!cursor.isAfterLast()){
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }
    public Receta getRow(Cursor c) {

        Receta ag = new Receta();
        if(c != null && c.moveToFirst()) {
            ag.setId(c.getInt(0));
            ag.setNombre(c.getString(1));
            ag.setInstrucciones(c.getString(2));
            ag.setFoto(Uri.parse(c.getString(3)));
        }else{
            System.out.println("faiiiiiiiil");
        }

        return ag;
    }

    public Receta getRow(int id) {
        String[] proyeccion = { Contrato.TablaReceta._ID,
                Contrato.TablaReceta.NOMBRE,
                Contrato.TablaReceta.INSTRUCCIONES,
                Contrato.TablaReceta.FOTO};
        String where = Contrato.TablaReceta._ID + " = ?";
        String[] parametros = new String[] { id+"" };
        String groupby = null; String having = null;
        String orderby = Contrato.TablaReceta.NOMBRE + " ASC";
        Cursor c = bd.query(Contrato.TablaReceta.TABLA, proyeccion,
                where, parametros, groupby, having, orderby);
        c.moveToFirst();
        Receta ag = getRow(c);
        c.close();
        return ag;
    }

    public Receta getRow2(int id) {
        String[] proyeccion = { Contrato.TablaReceta._ID,
                Contrato.TablaReceta.NOMBRE,
                Contrato.TablaReceta.INSTRUCCIONES,
                Contrato.TablaReceta.FOTO};
        String where = Contrato.TablaReceta._ID + " = ?";
        String[] parametros = new String[] { id+"" };
        String groupby = null; String having = null;
        String orderby = Contrato.TablaReceta.NOMBRE + " ASC";
        Cursor c = bd.query(Contrato.TablaReceta.TABLA, proyeccion,
                where, parametros, groupby, having, orderby);
        c.moveToFirst();
        Receta ag = getRow(c);
        c.close();
        return ag;
    }
    public Receta getRow(String nombre) {
        String[] parametros = new String[] { nombre };
        Cursor c = bd.rawQuery("select * from " +
                Contrato.TablaReceta.TABLA
                + " where " + Contrato.TablaReceta.NOMBRE + " = ?", parametros);
        c.moveToFirst();
        Receta ag = getRow(c);
        c.close();
        return ag;
    }

    /*
    public Receta getRow(Cursor c) {
        Receta ag = new Receta();
        ag.setId(c.getInt(c.getColumnIndex(Contrato.TablaReceta._ID)));
        ag.setNombre(c.getString(c.getColumnIndex(Contrato.TablaReceta.NOMBRE)));
        ag.setInstrucciones(c.getString(c.getColumnIndex(Contrato.TablaReceta.INSTRUCCIONES)));
        ag.setFoto(Uri.parse(c.getString(c.getColumnIndex(Contrato.TablaReceta.FOTO))));

        return ag;
    }
    */
    public Cursor getCursor(String condicion, String[] params) {
        Cursor cursor = bd.query(
                Contrato.TablaReceta.TABLA, null, condicion, params, null, null,
                Contrato.TablaReceta.NOMBRE);
        return cursor;
    }

    public Cursor getCursor(){
        Cursor cursor= getCursor(null,null);
        return cursor;
    }

    public List<Receta> select(String cond, String[] params){
        List<Receta> la= new ArrayList<>();
        Cursor cursor= getCursor(cond, params);
        Receta p;
        while(cursor.moveToNext()){
            p=getRow(cursor);
            la.add(p);
        }
        return la;
    }

    public List<Receta> select(){
        return select(null,null);
    }


}
