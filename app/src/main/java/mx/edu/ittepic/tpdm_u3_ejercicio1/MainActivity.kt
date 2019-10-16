package mx.edu.ittepic.tpdm_u3_ejercicio1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    var desc:EditText?=null
    var fecha:EditText?=null
    var lugar:EditText?=null
    var insertar:Button?=null
    var listView:ListView?=null

    //Declarando el objeto FIRESTORE
    var baseRemota=FirebaseFirestore.getInstance()
    //declarar objetos tipo ARREGLO DINAMICO
    var registrosRemotos=ArrayList<String>()
    var keys=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        desc=findViewById(R.id.desc)
        fecha=findViewById(R.id.fecha)
        lugar=findViewById(R.id.lugar)
        insertar=findViewById(R.id.insertar)
        listView=findViewById(R.id.lista)


        insertar?.setOnClickListener{
            //Asincronia de manera remota
         //crear registro para pasar todos los datos
            var datosInsertar= hashMapOf(
                "descripcion" to desc?.text.toString(),
                "fecha" to fecha?.text.toString(),
                "lugar" to lugar?.text.toString() )
        //conexion de bd; add --> insertar, eliminar -->.document(ID).(set ,delete,get) set=actualizar,delete=eliminar,get=obtener
            baseRemota.collection("eventos")
                .add(datosInsertar as Map<String,Any>) //as Map<String,Any>
                .addOnSuccessListener {//Respuesta satisfactoria de que si se inserto internet
                    Toast.makeText(this,"Inserción correcta",Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Error en inserción",Toast.LENGTH_LONG)
                        .show()

                }  //Cuando falla una insercion  internet
            limpiarCampos()
        }//Insertar
    }//onCreate

    fun limpiarCampos() {
        desc?.setText("")
        lugar?.setText("")
        fecha?.setText("")

    }
}
