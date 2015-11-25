package clasestabla;

import android.database.Cursor;

/**
 * Created by David on 17/11/2015.
 */
public class IngredienteReceta {

    private int id, idreceta, idingrediente, cantidad;

    /*************************************************************************/

    public IngredienteReceta(int id, int idreceta, int idingrediente, int cantidad) {
        this.id = id;
        this.idreceta = idreceta;
        this.idingrediente = idingrediente;
        this.cantidad = cantidad;
    }

    public IngredienteReceta() {
    }

    /*************************************************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdreceta() {
        return idreceta;
    }

    public void setIdreceta(int idreceta) {
        this.idreceta = idreceta;
    }

    public int getIdingrediente() {
        return idingrediente;
    }

    public void setIdingrediente(int idingrediente) {
        this.idingrediente = idingrediente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /*************************************************************************/

    @Override
    public String toString() {
        return "IngredienteReceta{" +
                "id=" + id +
                ", idreceta=" + idreceta +
                ", idingrediente=" + idingrediente +
                ", cantidad=" + cantidad +
                '}';
    }

    public void set(Cursor c) {
        setId(c.getInt(0));
        setIdingrediente(c.getInt(1));
        setIdreceta(c.getInt(2));
        setCantidad(c.getInt(3));
    }
}
