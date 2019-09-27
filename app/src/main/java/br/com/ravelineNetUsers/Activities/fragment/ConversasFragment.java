package br.com.ravelineNetUsers.Activities.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.Adapter.ConversasAdapter;
import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;
import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.Conversas;
import br.com.ravelineNetUsers.Activities.model.ConversasSalvas;
import br.com.ravelineNetUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<ConversasSalvas> conversasArrayAdapter;
    private ArrayList<ConversasSalvas> conversasArrayList;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_conversas, container, false);

        //montar listview e adapter
        conversasArrayList = new ArrayList<>();
        listView = view.findViewById(R.id.lv_fragment_conversas);
        conversasArrayAdapter = new ConversasAdapter(getActivity(),conversasArrayList);
        listView.setAdapter(conversasArrayAdapter);

        //recuperar dados do usuario
        PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(getActivity());
        String idUsuarioLogado = preferenciaUsuario.getIdentificador();

        //recuperar as conversas
        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("conversas").child(idUsuarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversasArrayList.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    ConversasSalvas conversasSalvasFragment = dados.getValue(ConversasSalvas.class);
                    conversasArrayList.add(conversasSalvasFragment);

                }
                conversasArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.addValueEventListener(valueEventListener);
    }
}
