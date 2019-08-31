package br.com.ravelineNetUsers.Activities.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import br.com.ravelineNetUsers.Activities.fragment.ContatosFragment;
import br.com.ravelineNetUsers.Activities.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] abasTitulo = {"CONVERSAS","CONTATOS"};


    public TabAdapter( FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ConversasFragment();
                break;

            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return abasTitulo.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return abasTitulo[position];
    }
}
