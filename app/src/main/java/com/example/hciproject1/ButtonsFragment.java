package com.example.hciproject1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class ButtonsFragment extends Fragment {

    static Fragment fragment;
    static Button button1, button2,button3;
    static LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_button, container, false);

        final Fragment f = getFragmentManager().findFragmentById(R.id.fragment2);

        linearLayout = view.findViewById(R.id.linear_layout_for_buttons);

        button1 = view.findViewById(R.id.button1);
        fragment = f;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.washCode = 1;
                BottomFragment.alterState(3, f);
            }
        });

        button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.washCode = 2;
                BottomFragment.alterState(3, f);
            }
        });

        button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.washCode = 3;
                BottomFragment.alterState(3, f);
            }
        });

        return view;
    }

    static void enableButtons(){
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        linearLayout.setForeground(null);
    }

    static void disableButtons(){
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        linearLayout.setForeground(FullscreenActivity.dim);
    }

}
