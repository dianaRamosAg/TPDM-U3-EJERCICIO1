package mx.edu.ittepic.tpdm_u3_ejercicio1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Main2Activity : AppCompatActivity() {
    var descA:EditText?=null
    var fechaA:EditText?=null
    var lugarA:EditText?=null
    var btnActualizar:Button?=null
    var btnRegresar:Button?=null

    var baseRemota=FirebaseFirestore.getInstance()
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        descA=findViewById(R.id.descA)
        fechaA=findViewById(R.id.fechaA)
        lugarA=findViewById(R.id.lugarA)
        btnActualizar=findViewById(R.id.btnActualizar)
        btnRegresar=findViewById(R.id.btnRegresar)

       id= intent.extras?.getString("id").toString()

        baseRemota.collection("eventos")
            .document(id)
            .get()
            .addOnSuccessListener {
                descA?.setText(it.getString("descripcion"))
                fechaA?.setText(it.getString("fecha"))
                lugarA?.setText(it.getString("lugar"))
            }
            .addOnFailureListener {
                descA?.setText("NULL")
                fechaA?.setText("NULL")
                lugarA?.setText("NO SE ENCONTRO DATO")

                descA?.isEnabled=false
                fechaA?.isEnabled=false
                lugarA?.isEnabled=false
                btnActualizar?.isEnabled=false
            }

        btnActualizar?.setOnClickListener() {
            var datosActualizar= hashMapOf(
                "descripcion" to descA?.text.toString(),
                "fecha" to fechaA?.text.toString(),
                "lugar" to lugarA?.text.toString()
            )

                baseRemota.collection("eventos")
                    .document(id)
                    .set(datosActualizar as Map <String,Any>)
                    .addOnSuccessListener {
                        limpiarCampos()
                        Toast.makeText(this,"SE ACTUALIZO", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"NO SE PUDO ACTUALIZAR", Toast.LENGTH_LONG).show()
                    }
        }//ACTUALIZAR

        btnRegresar?.setOnClickListener{finish()}

    }//OnCreate

    fun limpiarCampos() {
        descA?.setText("")
        fechaA?.setText("")
        lugarA?.setText("")

    }

}//Class
