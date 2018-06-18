package com.krintos.krintos.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import info.androidhive.loginandregistration.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Main_fragment extends Fragment {
    private Button btnMyAccout;
    private Button btnMath;
    private Button btnProg;
    private Button btnChem;
    private Button btnPhys;
    private Button btnPhil;
    private Button btnBio;
    private Button btnSet;
    private Button btntomain;



    public Main_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Krintos");
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_fragment, container, false);

        btnMyAccout = (Button) rootView.findViewById(R.id.button);
        btnMath = (Button) rootView.findViewById(R.id.button2);
        btnProg = (Button) rootView.findViewById(R.id.button1);
        btnChem = (Button) rootView.findViewById(R.id.button4);
        btnPhys = (Button) rootView.findViewById(R.id.button3);
        btnPhil = (Button) rootView.findViewById(R.id.button6);
        btnBio = (Button) rootView.findViewById(R.id.button5);
        btnSet = (Button) rootView.findViewById(R.id.button7);

        btnMyAccout.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onMyAccountButtonClicked(v);
        }
        });
        btnMath.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onMathButtonClicked(v);
        }
        });

        btnProg.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onProgButtonClicked(v);
        }
        });
        btnChem.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onChemButtonClicked(v);
        }
        });

        btnPhys.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onPhysButtonClicked(v);
        }
        });
        btnPhil.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onPhilButtonClicked(v);
        }
        });
        btnBio.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onBioButtonClicked(v);
        }
        });
        btnSet.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v){
            onSetButtonClicked(v);
        }
        });
        return rootView;
    }
    public void onMyAccountButtonClicked(View view){
        Fragment fragment = null;
        fragment = new MyAccount();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    public void onMathButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Mathematics();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }
    public void onProgButtonClicked(View view){
        Fragment fragment = null;
        fragment = new ProgActivity();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    public void onChemButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Chemistry();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }
    public void onPhysButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Physics();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }
    public void onPhilButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Philology();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }
    public void onBioButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Biology();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }
    public void onSetButtonClicked(View view){
        Fragment fragment = null;
        fragment = new Setting();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

}
