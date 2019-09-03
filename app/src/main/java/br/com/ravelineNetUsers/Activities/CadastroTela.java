package br.com.ravelineNetUsers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import br.com.ravelineNetUsers.R;

public class CadastroTela extends AppCompatActivity {
    private Button botaoCadastrarFone;
    private EditText nomeCadastroFone;
    private EditText zipCadastroFone;
    private EditText dddCadastroFone;
    private EditText telefoneCadastroFone;
    private EditText codValidacaoFone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tela);
        botaoCadastrarFone = findViewById(R.id.botaoCadastrarFone);
        nomeCadastroFone = findViewById(R.id.nomeTelaCadastroFone);
        zipCadastroFone = findViewById(R.id.codigoPaisFone);
        dddCadastroFone = findViewById(R.id.estadoFone);
        telefoneCadastroFone = findViewById(R.id.telefoneFone);
        codValidacaoFone = findViewById(R.id.codValidaFone);

        //Definindo mascaras
        SimpleMaskFormatter mascaraZipCod = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter mascaraDDD = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter mascaraTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        SimpleMaskFormatter mascaraCodigoValida = new SimpleMaskFormatter("NNNNNNNNN");

        //recebendo os valores das mascaras
        MaskTextWatcher mascaraZipWatcher = new MaskTextWatcher(zipCadastroFone,mascaraZipCod);
        MaskTextWatcher mascaraDDDWatcher = new MaskTextWatcher(dddCadastroFone,mascaraDDD);
        MaskTextWatcher mascaraTelWatcher = new MaskTextWatcher(telefoneCadastroFone,mascaraTelefone);
        MaskTextWatcher mascaraCodValidacao = new MaskTextWatcher(codValidacaoFone,mascaraCodigoValida);

        //adicionando aos listeners
        zipCadastroFone.addTextChangedListener(mascaraZipWatcher);
        dddCadastroFone.addTextChangedListener(mascaraDDDWatcher);
        telefoneCadastroFone.addTextChangedListener(mascaraTelWatcher);
        codValidacaoFone.addTextChangedListener(mascaraCodValidacao);






        botaoCadastrarFone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codValidacaoFone.getText().toString().equalsIgnoreCase("000")){
                    Toast.makeText(getApplicationContext(), "Correto numero correto!", Toast.LENGTH_SHORT).show();
                } else{
                /*Intent intent = new Intent(getApplicationContext(),TelaLogin.class);
                startActivity(intent);
                finish();*/
                    Toast.makeText(CadastroTela.this, "Erro, numero errado.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
