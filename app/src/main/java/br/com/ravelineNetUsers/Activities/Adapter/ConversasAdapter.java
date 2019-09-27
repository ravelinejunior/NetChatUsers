package br.com.ravelineNetUsers.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.Conversas;
import br.com.ravelineNetUsers.Activities.model.ConversasSalvas;
import br.com.ravelineNetUsers.R;

public class ConversasAdapter extends ArrayAdapter<ConversasSalvas> {
    private Context context;
    private ArrayList<ConversasSalvas> conversasArrayList;


    public ConversasAdapter(Context c,  ArrayList<ConversasSalvas> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversasArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;

    //verifica se a lista está preenchida
        if (conversasArrayList != null ){
            //inicializar objeto para ser visualizado na view
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //montar a view atraves de um xml
            view = layoutInflater.inflate(R.layout.lista_conversas,parent,false);

            //recuperar os elementos de textview
            TextView nomeConversas = view.findViewById(R.id.tv_titulo_email_conversas);
            TextView ultimaConversa = view.findViewById((R.id.tv_subtitulo_email_conversas));

            ConversasSalvas conversas = conversasArrayList.get(position);
            nomeConversas.setText(conversas.getNome());
            ultimaConversa.setText(conversas.getMensagens());


        }

        return view;
    }
}
