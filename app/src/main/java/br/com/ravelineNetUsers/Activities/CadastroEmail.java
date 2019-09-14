package br.com.ravelineNetUsers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.helper.Base64Decode;
import br.com.ravelineNetUsers.Activities.model.Usuarios;
import br.com.ravelineNetUsers.R;

public class CadastroEmail extends AppCompatActivity {
    private EditText nomeCadastroEmailLogin;
    private EditText emailCadastroEmailLogin;
    private EditText senhaCadastroEmailLogin;
    private Button botaoCadastroEmailLogin;
    private FirebaseAuth autenticacao;
    public Usuarios usuarios = new Usuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_email);


        nomeCadastroEmailLogin = findViewById(R.id.nomeCadastroEmail);
        emailCadastroEmailLogin = findViewById(R.id.emailCadastroEmail);
        senhaCadastroEmailLogin = findViewById(R.id.senhaCadastroEmail);
        botaoCadastroEmailLogin = findViewById(R.id.botaoCadastroEmail);

        botaoCadastroEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recuperando informações da classe Usuarios

                usuarios.setNome(nomeCadastroEmailLogin.getText().toString());
                usuarios.setEmail(emailCadastroEmailLogin.getText().toString());
                usuarios.setSenha(senhaCadastroEmailLogin.getText().toString());
                cadastrarUsuario();

                //Log.i(usuarios.getNome(), "nome: ");
                //Log.i(usuarios.getEmail(), "email: ");
                //Log.i(usuarios.getSenha(), "senha: ");


            }
        });
    }

    public void cadastrarUsuario(){
    //objeto responsavel pela autenticação do firebase
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        //recuperando valores e cadastrando usuario no bd
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
            ).addOnCompleteListener(CadastroEmail.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                //metodo que comprova se usuario foi cadastrado
                if (task.isSuccessful()){

                    //recuperando usuario e UID do usuario para enviar para o banco de dados
                    usuarios.setId(task.getResult().getUser().getUid());

                    //criar String para passar valor codificado para String
                    String usuarioIdCodificada = Base64Decode.codificarTeste(usuarios.getEmail());
                    usuarios.setId(usuarioIdCodificada);
                    usuarios.salvarUsuarios();

                    //salvando dados do usuario no proprio telefone
                    PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(CadastroEmail.this);
                    preferenciaUsuario.salvarDados(usuarioIdCodificada,usuarios.getNome());

                    Toast.makeText(getApplicationContext(), "Usuario cadastrado", Toast.LENGTH_SHORT).show();

                    //deslogando usuario apos cadastrá-lo
                    //autenticacao.signOut();
                    Intent intent = new Intent(getApplicationContext(),TelaLogin.class);
                    startActivity(intent);
                    finish();


                    //Realizando tratamento de erro.
                } else{
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthEmailException e) {
                        erroExcecao = "Formato de email invalido.";
                        Toast.makeText(CadastroEmail.this, "Erro: "+erroExcecao, Toast.LENGTH_LONG).show();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Senha fraca. Digite uma senha com mais caracteres e numeros.";
                        Toast.makeText(CadastroEmail.this, "Erro: "+erroExcecao, Toast.LENGTH_LONG).show();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Formato do email invalido.";
                        Toast.makeText(CadastroEmail.this, "Erro: "+erroExcecao, Toast.LENGTH_LONG).show();

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Email já cadastrado.";
                        Toast.makeText(CadastroEmail.this, "Erro: "+erroExcecao, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        erroExcecao = "Erro genérico.";
                        Toast.makeText(CadastroEmail.this, "Erro: "+erroExcecao, Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
