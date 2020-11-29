//NOTE: HomeFragment describes the behaviour of all of the subject cards

package com.example.sharedspace;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import com.example.sharedspace.Calendar.CalendarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private DummyController controller;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Users");

        Button butt = view.findViewById(R.id.message_butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubjectActivity.class);
                startActivity(intent);
            }
        });

        Button cbutt = view.findViewById(R.id.calendar_butt);
        cbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        controller = DummyController.getInstance();
        List<Subject> slist = controller.getUserSubjects();

        //boilerplate code for initiating a recyclerview
        RecyclerView rvSubject = (RecyclerView) getView().findViewById(R.id.home_recyclerview);
        SubjectAdapterNew subjectAdapter = new SubjectAdapterNew(slist);
        rvSubject.setAdapter(subjectAdapter);
        rvSubject.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}