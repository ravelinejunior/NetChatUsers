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
import java.util.EventListener;

import br.com.ravelineNetUsers.Activities.Adapter.MensagemAdapter;
import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.helper.Base64Decode;
import br.com.ravelineNetUsers.Activities.model.ConversasSalvas;
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
     private String nomeUsuarioDestinatario;
     private String idUsuarioDestinatario;


    //area de dados usuario remetente
     private String idUsuarioRemetente;
     private String nomeUsuarioRemetente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        btn_enviar_mensagem = findViewById(R.id.botao_enviar_mensagem);
        mensagem_conversas = findViewById(R.id.mensagem_id);
        listView = findViewById(R.id.listv_conversas);

        //recuperando dados do usuario
        final PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(Conversas.this);
        idUsuarioRemetente = preferenciaUsuario.getIdentificador();
        nomeUsuarioRemetente = preferenciaUsuario.getNome();

        //recuperando dados de outra activity (método Bundle)
        Bundle extraBundle = getIntent().getExtras();

        //conferindo se objeto Bundle está vazio
        if( extraBundle != null){
            nomeUsuarioDestinatario = extraBundle.getString("nome");

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
            toolbar.setTitle(nomeUsuarioDestinatario);
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

        listView.setAdapter(arrayAdapter);
        //recuperando as mensagens diretamente do Firebase
        firebase = ConfiguracaoFirebase.getReferenciaFirebase().
                child("mensagens").
                child(idUsuarioRemetente.toString()).
                child(idUsuarioDestinatario.toString());



        //criar listener para as mensagens
         valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpando as mensagens ao entrar no loop
                mensagens.clear();

                //percorrendo dataSnapshot para verificar filhos de referencias firebase
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    //criar objeto mensagem
                    Mensagens mensagem = dados.getValue(Mensagens.class);
                    mensagens.add(mensagem);

                }
                //notificando alteração de dados
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


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
                   Boolean retornoMensagemRemetente = salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);

                   //condição para verificar se mensagem foi enviada com sucesso
                    if(retornoMensagemRemetente){
                        //salvando mensagens de destinatario
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                        //verificando se mensagem foi recebida
                            if(retornoMensagemDestinatario){

                            }

                            else{
                                Toast.makeText(Conversas.this, "Via destinatário. Destinatário não recebeu esta mensagem!", Toast.LENGTH_LONG).show();
                            }

                    } else{
                        Toast.makeText(Conversas.this, "Via remetente. Não foi possível enviar essa mensagem.", Toast.LENGTH_SHORT).show();
                    }



                    //limpando o campo de texto quando o usuario clicar no botao de enviar
                    mensagem_conversas.setText("");

                    //salvar conversas remetente
                    //como a mensagem e o nome que serão exibidos sao dos destinatarios, quando passar id, passar do destinatario na instancia da classe ConversasSalvas
                    ConversasSalvas conversasSalvas = new ConversasSalvas();
                    conversasSalvas.setMensagens(mensagemEnviada);
                    conversasSalvas.setIdUsuario(idUsuarioDestinatario);
                    conversasSalvas.setNome(nomeUsuarioDestinatario);

                    Boolean retornoConversaRemetente = salvarConversas(idUsuarioRemetente,idUsuarioDestinatario,conversasSalvas);
                    if(retornoConversaRemetente){
                        //salvar conversas destinatario
                        ConversasSalvas conversasSalvasDestinatario = new ConversasSalvas();
                        conversasSalvasDestinatario.setIdUsuario(idUsuarioRemetente);
                        conversasSalvasDestinatario.setNome(nomeUsuarioRemetente);
                        conversasSalvasDestinatario.setMensagens(mensagemEnviada);

                        salvarConversas(idUsuarioDestinatario,idUsuarioRemetente,conversasSalvasDestinatario);
                    }else{
                        Toast.makeText(Conversas.this, "Não foi possivel salvar conversa.", Toast.LENGTH_SHORT).show();
                    }


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

    private boolean salvarMensagem(String idEmailRemetenteParam, String idEmailDestinatarioParam, Mensagens mensagem){
        try {

            //salvando mensagens no nó criado
            firebase = ConfiguracaoFirebase.getReferenciaFirebase().child("mensagens");

            //como as mensagens precisam de um identificador unico, chamar metodo PUSH para criação de um novo nó com identificador para cada mensagem
            firebase.child(idEmailRemetenteParam).
                    child(idEmailDestinatarioParam).
                    push().
                    setValue(mensagem);

            return true;


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //metodo para salvar as conversas dos usuarios
    private boolean salvarConversas(String idEmailRemetenteConversa, String idEmailDestinatarioConversa, ConversasSalvas mensagensConversa){

        try {

            //criando o nó para receber o ultimo valor de conversa
            //criar child de conversas
            firebase = ConfiguracaoFirebase.getReferenciaFirebase().child("conversas");
            firebase.child(idEmailRemetenteConversa).
                    child(idEmailDestinatarioConversa).
                    setValue(mensagensConversa);

            return true;

        }catch (Exception e){
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
