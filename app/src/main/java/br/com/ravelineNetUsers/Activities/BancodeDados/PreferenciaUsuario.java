package br.com.ravelineNetUsers.Activities.BancodeDados;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenciaUsuario {
     Context contexto ;
    private SharedPreferences sharedPreferences;
    private final String NOME_ARQUIVO = "NetChatUsers";
    private SharedPreferences.Editor editor;
    private final int MODE = 0;
    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    public PreferenciaUsuario(Context contextoParametro){

        this.contexto = contextoParametro;
        sharedPreferences = contextoParametro.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = sharedPreferences.edit();

    }

    public void salvarDados(String identificadorUsuario){

        editor.putString(CHAVE_IDENTIFICADOR,identificadorUsuario);
        editor.commit();

    }

    //metodo para retornar o identificador do usuario
    public String getIdentificador()
    {
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR,null);
    }


}
