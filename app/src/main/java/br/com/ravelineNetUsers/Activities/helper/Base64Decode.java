package br.com.ravelineNetUsers.Activities.helper;

import android.util.Base64;


public class Base64Decode {

    public static  String codificarTeste(String texto){
        byte[] data = null;
            data = texto.getBytes();
        return Base64.encodeToString(data, Base64.NO_WRAP).replaceAll(("\\n|\\r"),"");
    }

    //criar metodo estatico para codificar elemento do usuario
    public static String codificar (String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll(" (\\n|\\r) ","");
    }

    //criar metodo estico para decodificar elemento
    public static String decodificar(String textoCoficado){
        //Instanciar o return com String pra transformar o byte para String
        return new String(Base64.decode(textoCoficado,Base64.DEFAULT));
    }
}
