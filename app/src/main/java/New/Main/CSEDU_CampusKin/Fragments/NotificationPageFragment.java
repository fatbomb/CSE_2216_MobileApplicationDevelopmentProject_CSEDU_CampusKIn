package New.Main.CSEDU_CampusKin.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import New.Main.CSEDU_CampusKin.R;

public class NotificationPageFragment extends Fragment {

    public NotificationPageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_screen, container, false);
        TextView textView = view.findViewById(R.id.txtView);
        return view;
    }
}
