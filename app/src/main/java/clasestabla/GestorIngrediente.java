package clasestabla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import contrato.Ayudante;
import contrato.Contrato;

/**
 * Created by David on 18/11/2015.
 */
public class GestorIngrediente {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorIngrediente(Context c) {
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

    public long insert(Ingrediente ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaIngrediente.NOMBRE, ag.getNombre());
        long id = bd.insert(Contrato.TablaIngrediente.TABLA, null, valores);
        return id;
    }



    public int delete(Ingrediente ag) {

        return deleteId(ag.getId());
    }

    public int deleteId(long id) {
        String condicion = Contrato.TablaIngrediente._ID + " = ?";
        String[] argumentos = { id + "" };
        int cuenta = bd.delete(
                Contrato.TablaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }


    public int update(Ingrediente ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaIngrediente.NOMBRE, ag.getNombre());
        String condicion = Contrato.TablaIngrediente._ID + " = ?";
        String[] argumentos = { ag.getId() + "" };
        int cuenta = bd.update(Contrato.TablaIngrediente.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Ingrediente> select(String condicion) {
        List<Ingrediente> la = new ArrayList<Ingrediente>();
        Cursor cursor = bd.query(Contrato.TablaIngrediente.TABLA, null,
                condicion, null, null, null, null);
        cursor.moveToFirst();
        Ingrediente ag;
        while (!cursor.isAfterLast()) {
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }

    public List<Ingrediente> select2(String condicion, String[] params){
        List<Ingrediente> la= new ArrayList<>();
        Cursor cursor = bd.query(Contrato.TablaIngrediente.TABLA, null, condicion, params, null, null, null);
        cursor.moveToFirst();
        Ingrediente ag;
        while(!cursor.isAfterLast()){
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }
    public Ingrediente getRow(Cursor c) {

        Ingrediente ag = new Ingrediente();
//        if(c != null && c.moveToFirst()) {
            ag.setId(c.getInt(0));
            ag.setNombre(c.getString(1));

//        }else{
//            System.out.println("faiiiiiiilx2");
//        }
        return ag;
    }

    public Ingrediente getRow(long id) {
        String[] proyeccion = { Contrato.TablaIngrediente._ID,
                Contrato.TablaIngrediente.NOMBRE};
        String where = Contrato.TablaIngrediente._ID + " = ?";
        String[] parametros = new String[] { id+"" };
        String groupby = null; String having = null;
        String orderby = Contrato.TablaIngrediente.NOMBRE + " ASC";
        Cursor c = bd.query(Contrato.TablaIngrediente.TABLA, proyeccion,
                where, parametros, groupby, having, orderby);
        c.moveToFirst();
        Ingrediente ag = getRow(c);
        c.close();
        return ag;
    }

    public Ingrediente getRow(String nombre) {
        String[] parametros = new String[] { nombre };
        Cursor c = bd.rawQuery("select * from " +
                Contrato.TablaIngrediente.TABLA
                + " where " + Contrato.TablaIngrediente.NOMBRE + " = ?", parametros);
        c.moveToFirst();
        Ingrediente ag = getRow(c);
        c.close();
        return ag;
    }

    /*
    public Ingrediente getRow(Cursor c) {
        Ingrediente ag = new Ingrediente();
        ag.setId((int) c.getLong(c.getColumnIndex(Contrato.TablaIngrediente._ID)));
        ag.setNombre(c.getInt(c.getColumnIndex(Contrato.TablaIngrediente.NOMBRE)));
        return ag;
    }
    */

    public Cursor getCursor(String condicion, String[] params) {
        Cursor cursor = bd.query(
                Contrato.TablaIngrediente.TABLA, null, condicion, params, null, null, null);
        return cursor;
    }

    public Cursor getCursor(){
        Cursor cursor= getCursor(null, null);
        return cursor;
    }

    public List<Ingrediente> select(String cond, String[] params){
        List<Ingrediente> la= new ArrayList<>();
        Cursor cursor= getCursor(cond, params);
        Ingrediente p;
        while(cursor.moveToNext()){
            p=getRow(cursor);
            la.add(p);
        }
        return la;
    }

    public List<Ingrediente> select(){
        return select(null,null);
    }

    public Cursor nombresIngredientes(int id){
        String[] proyeccion= {Contrato.TablaIngrediente.NOMBRE};
        String where= Contrato.TablaIngrediente._ID + "= ?";
        String[] params= new String[]{id+""};
        Cursor c= bd.query(Contrato.TablaIngrediente.TABLA, null, where, params, null, null, null);

        return c;
    }
}
