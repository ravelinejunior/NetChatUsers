package br.com.ravelineNetUsers.Activities.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.Adapter.ContatoAdapter;
import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.Conversas;
import br.com.ravelineNetUsers.Activities.model.Contatos;
import br.com.ravelineNetUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Contatos> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    //criando metodos para otimizar o funcinamento do app

    @Override
    public void onStart() {

        //evento será inciado apenas quando fragmento for iniciado
        firebase.addValueEventListener(valueEventListenerContatos);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //instanciando os contatos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contatos, container, false);
        listView = view.findViewById(R.id.lv_fragment_contatos);

        /*
        arrayAdapter = new ArrayAdapter(

                getActivity(),
                R.layout.lista_contatos,
                contatos
        );
        */


        arrayAdapter = new ContatoAdapter(getActivity(),contatos);
        listView.setAdapter(arrayAdapter);

        //recuperando dados de identificação de preferencias
        PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(getActivity());
        String identificadorUsuarioLogado = preferenciaUsuario.getIdentificador();
        //recuperando a instancia FIREBASE
        firebase = ConfiguracaoFirebase.getReferenciaFirebase().child("contatos").child(identificadorUsuarioLogado);

        //utilizar um listener para recuperar contatos
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                //limpando a lista
                contatos.clear();

                //listar os contatos
                //getChildren pega o primeiro filho do nó getReference
                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    //cria objeto baseado no Contatos.class e armazena ele em contatos
                    Contatos contato = dados.getValue(Contatos.class);
                    contatos.add(contato);

                }

                //notificando o metodo adapter que houve alteração cadastral
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Conversas.class);

                //recuperando os dados do usuario para serem passados para proxima activity
                Contatos contatoActivity = contatos.get(i);

                //enviando dados para proxima activity
                intent.putExtra("nome",contatoActivity.getNome());
                intent.putExtra("email",contatoActivity.getEmail());


                startActivity(intent);
            }
        });

        return view;
    }

}
