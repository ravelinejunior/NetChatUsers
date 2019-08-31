package br.com.ravelineNetUsers.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.ravelineNetUsers.Activities.Adapter.TabAdapter;
import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.helper.Base64Decode;
import br.com.ravelineNetUsers.Activities.helper.SlidingTabLayout;
import br.com.ravelineNetUsers.Activities.model.Contatos;
import br.com.ravelineNetUsers.Activities.model.Usuarios;
import br.com.ravelineNetUsers.R;

public class MainActivity extends AppCompatActivity {
private String identificadorContato;
private FirebaseAuth auth;
private SlidingTabLayout slidingTabLayout;
private ViewPager viewPager;
private DatabaseReference databaseReference;
private Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      slidingTabLayout = findViewById(R.id.sld_tab);
      viewPager = findViewById(R.id.vp_novo);
      toolbar = findViewById(R.id.tb_conversas);

        auth = ConfiguracaoFirebase.getAutenticacaoFirebase();

        //configurando adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(),0);
        //recuperando os fragmentos
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorPrimary));


        //para deixar os slides distribuidos
        slidingTabLayout.setDistributeEvenly(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            slidingTabLayout.setLayoutMode(1);
        }
        slidingTabLayout.setHorizontalScrollBarEnabled(true);


        //carregando toolbar na tela principal
        setSupportActionBar(toolbar);
        //getSupportActionBar().hide();

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void deslogarUsuario(){
        auth = ConfiguracaoFirebase.getAutenticacaoFirebase();
        auth.signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(),TelaLogin.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //criando um objeto do tipo inflater(exibe menu)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    //Classe para retornar ação apos toque nos itens
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_sair:
            deslogarUsuario();
            return true;

            case R.id.item_pesquisa:
                Toast.makeText(this, "Item pesquisa", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_adicionarUsuario:
                Toast.makeText(this, "Adicionar Usuario", Toast.LENGTH_SHORT).show();
                adicionarUsuarioDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void adicionarUsuarioDialog(){

        //criando o alert dialog e definindo seus parametros
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo usuário");
        alertDialog.setMessage("Email do usuário");
        alertDialog.setCancelable(false);

        //Adicionando um edittext para que o usuario digite seu email
        final EditText textoEmail = new EditText(getApplicationContext());
       // textoEmail.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorEditText));
        alertDialog.setView(textoEmail);


        //definindo botões para o dialog
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //verificando se campo está vazio
                String emailDigitado = textoEmail.getText().toString();


                if(!emailDigitado.isEmpty()){
                    //verificando se usuario está cadastrado
                    identificadorContato = Base64Decode.codificarTeste(emailDigitado);
                    //identificadorContato = Base64Decode.codificarTeste(emailDigitado);
                    //recuperando a instancia do Firebase
                    databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("usuarios").child(identificadorContato);
                    Toast.makeText(MainActivity.this, "Salvo usuario: "+identificadorContato, Toast.LENGTH_LONG).show();

                    //realizando consulta no banco de dados
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                //recuperando dados do contato a ser adicionado, criar objeto e faze-lo receber objetos da classe Usuario
                                Usuarios usuarioContatoRecuperado = dataSnapshot.getValue(Usuarios.class);
                                //verificando se existem dados recuperados no dataSnapshot
                                PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(getApplicationContext());

                                //recuperando identiicador do usuario
                                String identificadorUsuarioLogado = preferenciaUsuario.getIdentificador();
                                //String identificadorContato = Base64Decode.codificar(usuarioContatoRecuperado.getEmail().toString());


                                //contato existe
                                auth.getCurrentUser().getEmail();
                                databaseReference = ConfiguracaoFirebase.getReferenciaFirebase();
                                databaseReference = databaseReference.child("contatos").child(identificadorUsuarioLogado).child(identificadorContato);


                                //teste Database
                            /*    DatabaseReference testeReference = databaseReference.getDatabase().getReference("contatos").
                                        child(identificadorContato).child(identificadorContato)
                                        ;*/

                                   /* testeReference.child("Identificador").setValue(identificadorContato);
                                    testeReference.child("Email").setValue(usuarioContatoRecuperado.getEmail());
                                    testeReference.child("Nome").setValue(usuarioContatoRecuperado.getNome());*/

   //                           configurando os dados do contato
                                Contatos contatos = new Contatos();
                                contatos.setIdentificador(identificadorContato);
                                contatos.setEmail(usuarioContatoRecuperado.getEmail());
                                contatos.setNome(usuarioContatoRecuperado.getNome());

                               databaseReference.setValue(contatos);
                            } else {

                                Toast.makeText(MainActivity.this, "Contato não existe.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //recuperando email de usuario logado
                        String emailUsuarioLogando = auth.getCurrentUser().getEmail().toString();


                        @Override
                        public void onCancelled( DatabaseError databaseError) {

                        }
                    });


                } else {

                        Toast.makeText(MainActivity.this, "Digite um email válido.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });



        //exibindo a Dialog
        alertDialog.create();
        alertDialog.show();



    }
}

















