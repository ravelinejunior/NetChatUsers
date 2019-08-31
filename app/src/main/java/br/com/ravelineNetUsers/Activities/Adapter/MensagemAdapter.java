package br.com.ravelineNetUsers.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.ravelineNetUsers.Activities.BancodeDados.PreferenciaUsuario;
import br.com.ravelineNetUsers.Activities.model.Mensagens;
import br.com.ravelineNetUsers.R;

public class MensagemAdapter extends ArrayAdapter<Mensagens> {

    private Context contexto;
    private ArrayList<Mensagens> mensagensArrayList;

    public MensagemAdapter(Context c, ArrayList<Mensagens> objects) {
        super(c, 0, objects);
        this.contexto = c;
        this.mensagensArrayList = objects;
    }


    @Override
    public View getView(int position, View viewConvertida, ViewGroup parent) {
            View view = null;

            //recuperando as mensagens enviadas
            Mensagens mensagens = mensagensArrayList.get(position);

            //verificando se existe uma lista de mensagens
            if(mensagensArrayList != null){

                //recuperando os dados do usuario remetente
                PreferenciaUsuario preferenciaUsuario = new PreferenciaUsuario(contexto);
                String idUsuarioRemetente = preferenciaUsuario.getIdentificador();

                //inicializar o objeto
                LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

                //montar layout a partir de um xml
                //view = inflater.inflate(R.layout.item_mensagem_esquerda,parent);

                //montando a view a partir do xml
                if (idUsuarioRemetente.equals(mensagens.getIdUsuario())){
                    //montar layout a partir de um xml
                    view = inflater.inflate(R.layout.item_mensagem_direita,parent,false);
                }else{
                    //montar layout a partir de um xml
                    view = inflater.inflate(R.layout.item_mensagem_esquerda,parent,false);
                }

                //recuperar o layout de mensagem
                TextView textoMensagemEsquerda = view.findViewById(R.id.texto_mensagem_id);
                textoMensagemEsquerda.setText(mensagens.getMensagemUsuario());
        }
                return view;
    }
}
