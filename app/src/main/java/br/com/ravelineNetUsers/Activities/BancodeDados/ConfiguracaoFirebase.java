package br.com.ravelineNetUsers.Activities.BancodeDados;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public final class ConfiguracaoFirebase {
  static  DatabaseReference referenciaFirebase = FirebaseDatabase.getInstance().getReference();
  static FirebaseAuth autenticacaoFirebase;
  static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static DatabaseReference getReferenciaFirebase() {

        if (referenciaFirebase == null) {
            //referenciaFirebase.child("pontos:").setValue(100);
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getAutenticacaoFirebase(){

        if (autenticacaoFirebase == null){
            autenticacaoFirebase = FirebaseAuth.getInstance();
        }

        return autenticacaoFirebase;
    }
}
