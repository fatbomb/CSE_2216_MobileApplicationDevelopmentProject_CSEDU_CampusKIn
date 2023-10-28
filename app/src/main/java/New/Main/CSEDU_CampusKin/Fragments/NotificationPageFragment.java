package New.Main.CSEDU_CampusKin.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import New.Main.CSEDU_CampusKin.Adapters.NotificationAdapter;
import New.Main.CSEDU_CampusKin.Model.NotificationModel;
import New.Main.CSEDU_CampusKin.R;

public class NotificationPageFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationModelList;

    public NotificationPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_screen, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationModelList = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(), notificationModelList);
        recyclerView.setAdapter(adapter);

        readNotifications();

        return view;
    }

    private void readNotifications(){
        FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    notificationModelList.add(snapshot.getValue(NotificationModel.class));
                }
                Collections.reverse(notificationModelList);
                adapter.notifyDataSetChanged();
                System.out.println("notification being read");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
