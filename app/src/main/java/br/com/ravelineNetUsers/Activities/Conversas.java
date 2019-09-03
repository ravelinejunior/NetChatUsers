package br.com.ravelineNetUsers.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.Adapter.MensagemAdapter;
import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.helper.Base64Decode;
import br.com.ravelineNetUsers.Activities.model.Mensagens;
import br.com.ravelineNetUsers.R;

public class Conversas extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton btn_enviar_mensagem;
    private EditText mensagem_conversas;
    protected DatabaseReference firebase;
    private ArrayList<Mensagens> mensagens;
    private ArrayAdapter<Mensagens> arrayAdapter;
    private ListView listView;
    private ValueEventListener valueEventListenerMensagem;

    //area de dados do usuario destinatario
     private String nomeUsuario;
     private String idUsuarioDestinatario;


    //area de dados usuario remetente
     private String idUsuarioRemetente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        btn_enviar_mensagem = findViewById(R.id.botao_enviar_mensagem);
        mensagem_conversas = findViewById(R.id.mensagem_id);
        listView = findViewById(R.id.listv_conversas);


        //recuperando dados de outra activity (método Bundle)
        Bundle extraBundle = getIntent().getExtras();

        //conferindo se objeto Bundle está vazio
        if( extraBundle != null){
            nomeUsuario = extraBundle.getString("nome");

            //recuperando email via extraBundle já convertido
            String emailDestinatario = extraBundle.getString("email");
            idUsuarioDestinatario = Base64Decode.codificarTeste(emailDestinatario);
        }

        toolbar = findViewById(R.id.tb_conversas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setLogo(R.drawable.ic_account_circle);
            toolbar.setTitle(nomeUsuario);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        //criando array e adapter para exibição de mensagens
        mensagens = new ArrayList<>();

        //criando um adapter personalizado
        arrayAdapter = new MensagemAdapter(getApplicationContext(),mensagens);

    /*    arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                mensagens
        );*/

        listView.setAdapter(arrayAdapter);


        //criar listener para as mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpando as mensagens ao entrar no loop
                mensagens.clear();

                //percorrendo dataSnapshot para verificar filhos de referencias firebase
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    //criar objeto mensagem
                    Mensagens mensagem = dados.getValue(Mensagens.class);
                    mensagens.add(mensagem);

                }
                //notificando alteração de dados
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        };


        //recuperando dados do usuario
        PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(Conversas.this);
        idUsuarioRemetente = preferenciaUsuario.getIdentificador();


        //recuperando as mensagens diretamente do Firebase
        firebase = ConfiguracaoFirebase.getReferenciaFirebase().child("mensagens").child(idUsuarioRemetente.toString()).child(idUsuarioDestinatario.toString());


        //enviando mensagem
        firebase.addValueEventListener(valueEventListenerMensagem);

        //criando evento para envio de mensagem
        btn_enviar_mensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensagemEnviada = mensagem_conversas.getText().toString();

                //verificação se existe mensagem no campo digitado
                if(mensagemEnviada.isEmpty()){

                    Toast.makeText(Conversas.this, "Digite alguma mensagem seu idiota.", Toast.LENGTH_SHORT).show();

                } else{

                    //recuperando e salvando mensagens digitadas
                    Mensagens mensagem = new Mensagens();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagemUsuario(mensagemEnviada);

                    //salvando mensagens de remetente
                    salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);

                    //salvando mensagens de destinatario
                    salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);

                    //limpando o campo de texto quando o usuario clicar no botao de enviar
                    mensagem_conversas.setText("");

                    /* ESTRUTURA DE ENVIO DE MENSAGEM
                    MENSAGENS
                    * USUARIO REMETENTE
                    *  EMAIL REMETENTE
                    *   MENSAGEM
                    *
                    * USUARIO DESTINATARIO
                    *  EMAIL DESTINATARIO
                    *   MENSAGEM
                    *
                    *
                    * */

                }
            }
        });

    }

    private boolean salvarMensagem(String id, String email, Mensagens mensagem){
        try {

            //salvando mensagens no nó criado
            firebase = ConfiguracaoFirebase.getReferenciaFirebase().child("mensagens");

            //como as mensagens precisam de um identificador unico, chamar metodo PUSH para criação de um novo nó com identificador para cada mensagem
            firebase.child(idUsuarioRemetente).
                    child(idUsuarioDestinatario).
                    push().
                    setValue(mensagem);

            return true;


        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        //economizando recursos caso usuario nao esteja utilizando a tela de mensagens. Pode fazer o app demorar para carregar os dados
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }
}
