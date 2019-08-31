package br.com.ravelineNetUsers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.helper.Base64Decode;
import br.com.ravelineNetUsers.Activities.model.Usuarios;
import br.com.ravelineNetUsers.R;

public class TelaLogin extends AppCompatActivity {
    private Button botaoLogar;
    private EditText usuarioLogin;
    private EditText senhaLogin;
    private TextView textoCadastroLogin;
    Usuarios usuarios;
    FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);


        //chamando metodo para verificar se ja existe usuario logado no sistema
        verificarUsuarioLogado();

        botaoLogar = findViewById(R.id.botaoLogarLogin);
        usuarioLogin = findViewById(R.id.nomeUsuario);
        senhaLogin = findViewById(R.id.senhaUsuario);
        textoCadastroLogin = findViewById(R.id.textoCadastro);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarios = new Usuarios();
                usuarios.setEmail(usuarioLogin.getText().toString());
                usuarios.setSenha(senhaLogin.getText().toString());
                validarUsuario();
            }
        });




    }

    //Metodo para verificar se existe usuario logado
    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        //verificação de usuario atual
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    //Metodo criado ao clicar no texto ir para tela de cadastro
    public void abrirCadastroEmail(View view){
        Intent intent = new Intent(TelaLogin.this,CadastroEmail.class);
        startActivity(intent);
        finish();
    }

    public void validarUsuario(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        autenticacao.signInWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if(task.isSuccessful()){


                    //salvando dados do usuario no proprio telefone
                    PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(getApplicationContext());
                    String identificadorUsuarioLogado = Base64Decode.codificarTeste(usuarios.getEmail());
                    preferenciaUsuario.salvarDados(identificadorUsuarioLogado);

                    //abrindo tela principal
                    Toast.makeText(TelaLogin.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();

                }
                else{
                    Toast.makeText(TelaLogin.this, "Senha ou email incorretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
