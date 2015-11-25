package contrato;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by David on 17/11/2015.
 */
public class Ayudante extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recetario.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql;

        sql = "create table " + Contrato.TablaReceta.TABLA +
                " (" + Contrato.TablaReceta._ID +
                " integer primary key autoincrement, " +
                Contrato.TablaReceta.NOMBRE + " text, " +
                Contrato.TablaReceta.INSTRUCCIONES + " text, " +
                Contrato.TablaReceta.FOTO + " text)";

        db.execSQL(sql);

        sql = "create table " + Contrato.TablaIngrediente.TABLA +
                " (" + Contrato.TablaIngrediente._ID +
                " integer primary key autoincrement, " +
                Contrato.TablaIngrediente.NOMBRE + " text)";

        db.execSQL(sql);

        sql = "create table " + Contrato.TablaRecetaIngrediente.TABLA +
                " (" + Contrato.TablaReceta._ID +
                " integer primary key autoincrement, " +
                Contrato.TablaRecetaIngrediente.IDINGREDIENTE + " integer, " +
                Contrato.TablaRecetaIngrediente.IDRECETA + " integer, " +
                Contrato.TablaRecetaIngrediente.CANTIDAD + " integer)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "drop table if exists "
                + Contrato.TablaReceta.TABLA;
        db.execSQL(sql);

        sql = "drop table if exists "
                + Contrato.TablaIngrediente.TABLA;
        db.execSQL(sql);

        sql = "drop table if exists "
                + Contrato.TablaRecetaIngrediente.TABLA;
        db.execSQL(sql);

        onCreate(db);

    }
}
