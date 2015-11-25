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
 * Created by David on 20/11/2015.
 */
public class GestorIngredienteReceta {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorIngredienteReceta(Context c) {
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

    public long insert(IngredienteReceta ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaRecetaIngrediente.CANTIDAD,
                ag.getCantidad());
        valores.put(Contrato.TablaRecetaIngrediente.IDINGREDIENTE,
                ag.getIdingrediente());
        valores.put(Contrato.TablaRecetaIngrediente.IDRECETA,
                ag.getIdreceta());
        long id = bd.insert(Contrato.TablaRecetaIngrediente.TABLA,
                null, valores);
        return id;

        // Devuelve -1 si falla.
    }



    public int delete(IngredienteReceta ag) {

        return deleteId(ag.getId());
    }

    public int deleteId(long id) {
        String condicion = Contrato.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { id + "" };
        int cuenta = bd.delete(
                Contrato.TablaRecetaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }


    public int update(IngredienteReceta ag) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaRecetaIngrediente.CANTIDAD, ag.getCantidad());
        valores.put(Contrato.TablaRecetaIngrediente.IDINGREDIENTE, ag.getIdingrediente());
        valores.put(Contrato.TablaRecetaIngrediente.IDRECETA, ag.getIdreceta());
        String condicion = Contrato.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { ag.getId() + "" };
        int cuenta = bd.update(Contrato.TablaRecetaIngrediente.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    public List<IngredienteReceta> select(String condicion) {
        List<IngredienteReceta> la = new ArrayList<IngredienteReceta>();
        Cursor cursor = bd.query(Contrato.TablaRecetaIngrediente.TABLA, null,
                condicion, null, null, null, null);
        cursor.moveToFirst();
        IngredienteReceta ag;
        while (!cursor.isAfterLast()) {
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }

    public List<IngredienteReceta> select2(String condicion, String[] params){
        List<IngredienteReceta> la= new ArrayList<>();
        Cursor cursor = bd.query(Contrato.TablaRecetaIngrediente.TABLA, null, condicion, params, null, null, null);
        cursor.moveToFirst();
        IngredienteReceta ag;
        while(!cursor.isAfterLast()){
            ag = getRow(cursor);
            la.add(ag);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }
    public IngredienteReceta getRow(Cursor c) {

        IngredienteReceta ag = new IngredienteReceta();
        ag.setId(c.getInt(0));
        ag.setIdingrediente(c.getInt(1));
        ag.setIdreceta(c.getInt(2));
        ag.setCantidad(c.getInt(3));

        return ag;
    }

    public IngredienteReceta getRow2(long id) {
        String[] proyeccion = { Contrato.TablaRecetaIngrediente._ID,
                Contrato.TablaRecetaIngrediente.IDINGREDIENTE,
                Contrato.TablaRecetaIngrediente.IDRECETA,
                Contrato.TablaRecetaIngrediente.CANTIDAD};
        String where = Contrato.TablaRecetaIngrediente._ID + " = ?";
        String[] parametros = new String[] { id+"" };
        String groupby = null; String having = null;
        String orderby = null;
        Cursor c = bd.query(Contrato.TablaRecetaIngrediente.TABLA, proyeccion,
                where, parametros, groupby, having, orderby);
        c.moveToFirst();
        IngredienteReceta ag = getRow(c);
        c.close();
        return ag;
    }

    /*
    public IngredienteReceta getRow3(String nombre) {
        String[] parametros = new String[] { nombre };
        Cursor c = bd.rawQuery("select * from " +
                Contrato.TablaRecetaIngrediente.TABLA
                + " where " + Contrato.TablaRecetaIngrediente.TITULO + " = ?", parametros);
        c.moveToFirst();
        IngredienteReceta ag = getRow(c);
        c.close();
        return ag;
    }
*/
    public IngredienteReceta getRow4(Cursor c) {
        IngredienteReceta ag = new IngredienteReceta();
        ag.setId(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente._ID)));
        ag.setIdingrediente(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDINGREDIENTE)));
        ag.setIdreceta(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente.IDRECETA)));
        ag.setCantidad(c.getInt(c.getColumnIndex(Contrato.TablaRecetaIngrediente.CANTIDAD)));

        return ag;
    }

    public Cursor getCursor(String condicion, String[] params) {
        Cursor cursor = bd.query(
                Contrato.TablaRecetaIngrediente.TABLA, null, condicion, params, null, null,
                Contrato.TablaRecetaIngrediente.IDINGREDIENTE+", "+Contrato.TablaRecetaIngrediente.IDRECETA);
        return cursor;
    }

    public Cursor getCursor(){
        Cursor cursor= getCursor(null, null);
        return cursor;
    }

    public List<IngredienteReceta> select(String cond, String[] params){
        List<IngredienteReceta> la= new ArrayList<>();
        Cursor cursor= getCursor(cond, params);
        IngredienteReceta p;
        while(cursor.moveToNext()){
            p=getRow(cursor);
            la.add(p);
        }
        return la;
    }

    public List<IngredienteReceta> select(){
        return select(null,null);
    }

    public Cursor selectIngredientes(int id){

        String[] proyeccion= {Contrato.TablaRecetaIngrediente.IDINGREDIENTE};
        String where= Contrato.TablaRecetaIngrediente.IDRECETA + "= ?";
        String[] params= new String[]{id+""};
        Cursor c= bd.query(Contrato.TablaRecetaIngrediente.TABLA, null, where, params, null, null, null);

        return c;
    }
}
