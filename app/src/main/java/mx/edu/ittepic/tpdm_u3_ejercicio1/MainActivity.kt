package mx.edu.ittepic.tpdm_u3_ejercicio1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
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

        baseRemota.collection("eventos")
            .addSnapshotListener { querySnapshot, e ->
                if(e!=null){
                    Toast.makeText(this,"Error no se puede consultar",Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                registrosRemotos.clear()
                keys.clear()
                for(document in querySnapshot!!){
                    var cadena= document.getString("descripcion")+"\n"+
                                       document.getString("fecha")+"--"+
                                       document.getString("lugar")
                        registrosRemotos.add(cadena)
                        keys.add(document.id)
                }
                if(registrosRemotos.size==0){registrosRemotos.add("NO HAY DATOS AUN PARA MOSTRAR")}
                var adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,registrosRemotos)
                listView?.adapter=adapter
            }

        listView?.setOnItemClickListener { parent, view, position, id ->
            if(keys.size==0){return@setOnItemClickListener}

            AlertDialog.Builder(this).setTitle("ATENION")
                .setMessage("¿Qué deseas hacer con : "+registrosRemotos.get(position)+" ?")
                .setPositiveButton("Eliminar"){dialog,which->
                    baseRemota.collection("eventos")
                        .document(keys.get(position)).delete()  //Eliminar Registro
                        .addOnSuccessListener {
                            Toast.makeText(this,"Evento eliminado",Toast.LENGTH_SHORT)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"Evento no eliminado",Toast.LENGTH_SHORT)
                        }
                }
                .setNegativeButton("Actualizar"){dialog,which->
                    var nuevaVentana=Intent(this,Main2Activity::class.java)
                    nuevaVentana.putExtra("id",keys.get(position))
                    startActivity(nuevaVentana)

                }
                .setNeutralButton("Cancelar"){dialog,which->}
                .show()

        }


    }//onCreate

    fun limpiarCampos() {
        desc?.setText("")
        lugar?.setText("")
        fecha?.setText("")

    }
}
