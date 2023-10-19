package New.Main.CSEDU_CampusKin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import New.Main.CSEDU_CampusKin.R;


public class ChatPageFragment extends Fragment {

    RecyclerView recyclerView;
    public ChatPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.chat_screen_fragment, container, false);
        recyclerView = view.findViewById(R.id.chat_list_recycler_view);

        return  view;
    }
}