package com.example.sharedspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    BottomNavigationView navigationView;

    RecyclerView recyclerViewRooms;
    Subject thisSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        actionBar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.navigation);
        //navigationView.setOnNavigationItemSelectedListener(selectedListener);

        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);

        thisSubject = new Subject("500001", "info sys");

        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(thisSubject);
        // Attach the adapter to the recyclerview to populate items
        recyclerViewRooms.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
    }



    // Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
        // Store a member variable for the contacts
        private Subject mSubject;
        private List<Room> mThisRoomList;

        // Pass in the contact array into the constructor
        public ContactsAdapter(Subject subject) {
            mSubject = subject;
            mThisRoomList = subject.getRoomList();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.room_card, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        /*
        * TODO: This method integrates the Rooms info onto the UI cards
        *  that represents individual rooms on SubjectActivity Page
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Get the data model based on position
            Room room = mThisRoomList.get(position);

            // Set item views based on your views and data model
            TextView roomTitleTextView = holder.roomTitleTextView;
            TextView timeClosedTextView = holder.timeClosedTextView;
            TextView numberPeopleTextView = holder.numberPeopleTextView;
            Button button = holder.joinRoomButton;

            roomTitleTextView.setText(room.getTitle());
            timeClosedTextView.setText("Closed at "+room.getTimeStarted());
            numberPeopleTextView.setText(String.valueOf(room.getSizeOfRoom()));
            button.setText(room.isFull() ? "Room Full":"Join Room");
            button.setEnabled(!room.isFull());
        }

        @Override
        public int getItemCount() {
            return mThisRoomList.size();
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView roomTitleTextView, numberPeopleTextView, timeClosedTextView;
            public Button joinRoomButton;


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                roomTitleTextView =  itemView.findViewById(R.id.room_title);
                joinRoomButton =  itemView.findViewById(R.id.join_room_button);
                numberPeopleTextView = itemView.findViewById(R.id.number_of_people);
                timeClosedTextView =  itemView.findViewById(R.id.time_closed);
            }
        }
    }
}