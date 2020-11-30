package com.example.sharedspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    // firebase auth
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    BottomNavigationView navigationView;
    ListView mListViewSubjects;
    ListView fakelistview;
    final static String SUBJECT_TYPE="subject";
    final static String SUBJECT_TITLE = "title";
    ArrayList<Subject> subjectList;
    SubjectAdapter subjectAdapter;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        actionBar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();
        mListViewSubjects = findViewById(R.id.ListViewSubjects);
        fakelistview = findViewById(R.id.fakeListViewSubjects);
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);


        //a temporary list. testing queries for specific things
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("subjects");
        final List<String> testList = Arrays.asList("50001", "50002");
        subjectList = new ArrayList<>();
        for (String courseType : testList){
            Query query = mDatabase.orderByChild("courseType").equalTo(courseType);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Subject subject = snapshot.getValue(Subject.class);
                        subjectList.add(subject);
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        subjectAdapter = new SubjectAdapter(this, subjectList);
        mListViewSubjects.setAdapter(subjectAdapter);

        //TODO: useful for adding new objects manually
        //ArrayList<Subject> subjectArrayList = new ArrayList<>();
//        subjectArrayList.add(new Subject("50.001","50001","Introduction to Information Systems and Programming"));
//        subjectArrayList.add(new Subject("50.002","50002","Computer Structures"));
//        subjectArrayList.add(new Subject("50.004","50004","Introduction to Algorithms"));

        //for (Subject sub:subjectArrayList) mDatabase.child(sub.getCourseType()).setValue(sub);
        //FirebaseDatabase.getInstance().getReference().child("messages").child("10000").setValue("messages");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            actionBar.setTitle("Home");
                            EmptyFragment emptyFragment = new EmptyFragment();
                            FragmentTransaction hft = getSupportFragmentManager().beginTransaction();
                            hft.replace(R.id.content, emptyFragment, "");
                            hft.addToBackStack(null);
                            hft.commit();
                            mListViewSubjects.setVisibility(View.VISIBLE);

                            //removes ALL fragments, should show the basic layout of dashboard activity
//                            for (Fragment fragment: getSupportFragmentManager().getFragments()){
//                                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                            }
                            return true;

                        case R.id.nav_profile:
                            // profile fragment transaction
                            actionBar.setTitle("Profile");
                            ProfileFragment profileFragment = new ProfileFragment();
                            FragmentTransaction pft = getSupportFragmentManager().beginTransaction();
                            pft.replace(R.id.content, profileFragment, "");
                            pft.addToBackStack(null);
                            pft.commit();

                            mListViewSubjects.setVisibility(View.INVISIBLE);
                            return true;

                        case R.id.nav_friends:
                            // users fragment transaction
                            actionBar.setTitle("Friends");
                            FriendsFragment friendFragment = new FriendsFragment();
                            FragmentTransaction fft = getSupportFragmentManager().beginTransaction();
                            fft.replace(R.id.content, friendFragment, "");
                            fft.addToBackStack(null);
                            fft.commit();

                            mListViewSubjects.setVisibility(View.INVISIBLE);
                            return true;
                    }
                    return false;
                }
            };

    public class SubjectAdapter extends ArrayAdapter<Subject> {
        public SubjectAdapter(Context context, ArrayList<Subject> subjects){
            super(context, 0, subjects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Subject subject = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_card, parent, false);
            }
            // Lookup view for data population
            TextView cardHeaderTextView = convertView.findViewById(R.id.card_header);
            TextView cardSubjectIDTextView = convertView.findViewById(R.id.card_subjectID);
            TextView cardStudyingTextView = convertView.findViewById(R.id.card_studying);
            CardView subjectCard = convertView.findViewById(R.id.subject_card_view);

            // Populate the data into the template view using the data object
            cardHeaderTextView.setText(subject.getCourseTitle());
            cardSubjectIDTextView.setText(subject.getCourseID());
            cardStudyingTextView.setText(String.valueOf(subject.getRoomList().size()));
            subjectCard.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(SubjectActivity.this, RoomListActivity.class);
                   intent.putExtra(SubjectActivity.SUBJECT_TYPE, subject.getCourseType());
                   intent.putExtra(SubjectActivity.SUBJECT_TITLE, subject.getCourseTitle());
                   SubjectActivity.this.startActivity(intent);
               }
            });
            // Return the completed view to render on screen
            return convertView;
        }
    }
}