package br.com.ravelineNetUsers.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.model.Contatos;
import br.com.ravelineNetUsers.R;


public class ContatoAdapter extends ArrayAdapter<Contatos> {

    private ArrayList<Contatos> contatos;
    private Context context;

    public ContatoAdapter(Context c, ArrayList<Contatos> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    public View getView (int position , View convertView , ViewGroup parent){

            View view = null;

            //verificando se a lista está vazia
            if(contatos != null){

                //inicializar a view
                //acessando serviços do sistema (serviço de montagem de layout)
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

                //montar view atraves do xml
                view = inflater.inflate(R.layout.lista_contatos,parent,false);

                //recuperando elemento para exibição
                TextView nomeContato = view.findViewById(R.id.tv_nome_id);
                TextView emailContato = view.findViewById(R.id.tv_email_id);
                ImageView imagemContato = view.findViewById(R.id.imview_model);

                Contatos contato = contatos.get(position);
                nomeContato.setText(contato.getNome());
                emailContato.setText(contato.getEmail());



            }
        return view;
    }
}
