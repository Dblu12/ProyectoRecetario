package clasestabla;

import android.net.Uri;

/**
 * Created by David on 17/11/2015.
 */
public class Receta {

    private int id;
    private String nombre, instrucciones;
    private Uri foto;

    /*************************************************************************/

    public Receta(int id, String nombre, String instrucciones, Uri foto) {
        this.id = id;
        this.nombre = nombre;
        this.instrucciones = instrucciones;
        this.foto = foto;
    }

    public Receta() {
    }

    /*************************************************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    /*************************************************************************/

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", instrucciones='" + instrucciones + '\'' +
                ", foto=" + foto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receta receta = (Receta) o;

        if (id != receta.id) return false;
        return nombre.equals(receta.nombre);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + nombre.hashCode();
        return result;
    }
}
