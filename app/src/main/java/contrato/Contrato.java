package contrato;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by David on 17/11/2015.
 */
public class Contrato {
    private Contrato (){
    }
    public static abstract class TablaReceta implements
            BaseColumns {
        public static final String TABLA = "receta";
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
        public static final String INSTRUCCIONES = "instrucciones";
    }

    public static abstract class TablaIngrediente implements BaseColumns{
        public static final String TABLA = "ingrediente";
        public static final String NOMBRE = "nombre";
    }

    public static abstract class TablaRecetaIngrediente implements BaseColumns{
        public static final String TABLA = "recetaingrediente";
        public static final String IDRECETA= "idreceta";
        public static final String IDINGREDIENTE= "idingrediente";
        public static final String CANTIDAD= "cantidad";
    }
}
