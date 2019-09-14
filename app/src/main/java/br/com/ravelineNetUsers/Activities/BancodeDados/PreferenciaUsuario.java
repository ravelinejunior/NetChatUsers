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
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public PreferenciaUsuario(Context contextoParametro){

        this.contexto = contextoParametro;
        sharedPreferences = contextoParametro.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = sharedPreferences.edit();

    }

    public void salvarDados(String identificadorUsuario, String nomeUsuarioLogado){

        editor.putString(CHAVE_IDENTIFICADOR,identificadorUsuario);
        editor.putString(CHAVE_NOME,nomeUsuarioLogado);
        editor.commit();

    }

    //metodo para retornar o identificador do usuario
    public String getIdentificador()
    {
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR,null);
    }

    //metodo de retorno para nome usuario
    public String getNome()
    {
        return sharedPreferences.getString(CHAVE_NOME,null);
    }


}
