package com.example.hciproject1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TopFragment extends Fragment {

    static StateProgressBar b1, b2, b3, b4, b5;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_fragment, container, false);
        b1 = view.findViewById(R.id.your_state_progress_bar_id1);
        b2 = view.findViewById(R.id.your_state_progress_bar_id2);
        b3 = view.findViewById(R.id.your_state_progress_bar_id3);
        b4 = view.findViewById(R.id.your_state_progress_bar_id4);
        b5 = view.findViewById(R.id.your_state_progress_bar_id5);
        return view;
    }

    static void visibleOnly(int which) {
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        b4.setVisibility(View.GONE);
        b5.setVisibility(View.GONE);
        switch (which){
            case 1:
                b1.setVisibility(View.VISIBLE);
                break;
            case 2:
                b2.setVisibility(View.VISIBLE);
                break;
            case 3:
                b3.setVisibility(View.VISIBLE);
                break;
            case 4:
                b4.setVisibility(View.VISIBLE);
                break;
            case 5:
                b5.setVisibility(View.VISIBLE);
                break;
        }
    }

}
