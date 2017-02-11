package com.chchi.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chchi.todo.FireBaseUtils.Firebase;
import com.chchi.todo.FragmentController.EditItemFragment;
import com.chchi.todo.ListViewController.Todo;
import com.chchi.todo.ListViewController.TodoViewHolder;
import com.chchi.todo.SignInActivity.SignInActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {
    private static final String USER_CHILD = "users";
    // Firebase instance variables
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    public DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Todo, TodoViewHolder>
            mFirebaseAdapter;
    private FirebaseStorage mFirebaseStorage;
    private ProgressBar mProgressBar;
    private RecyclerView mTodoRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ListView list = (ListView) findViewById(R.id.Listview);
//        list.setAdapter(new TodoAdaptor(this));
        // Initialize Firebase Auth
        Firebase.getDatabase();
        mFirebaseAuth = Firebase.getFirebaseAuth();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = (new Intent(this, SignInActivity.class));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        } else {
//            mUsername = mFirebaseUser.getDisplayName();
//            if (mFirebaseUser.getPhotoUrl() != null) {
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
//                startActivity(intent);
                showEditDialog();
            }
        });

        // Initialize SwipeToRefreshLayout.
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Here to refetch the data on server.

                Toast.makeText(getApplicationContext(),"onRefresh Called", Toast.LENGTH_LONG).show();
                //To end the spinning animation.
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTodoRecyclerView = (RecyclerView) findViewById(R.id.TodoRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setReverseLayout(true);
        mTodoRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTodoRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mFirebaseDatabaseReference = Firebase.getDatabase().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Todo,
                TodoViewHolder>(
                Todo.class,
                R.layout.todo,
                TodoViewHolder.class,
                mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid())) {
            @Override
            protected void populateViewHolder(TodoViewHolder viewHolder,
                                              Todo Todo, final int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.title.setText(Todo.getTitle());
                viewHolder.description.setText(Todo.getDescription());
                viewHolder.date.setText(Todo.getDate());
                viewHolder.time.setText(Todo.getTime());
                if (Todo.getPriority() == null || Todo.getPriority().isEmpty()) {
                    viewHolder.priority
                            .setImageDrawable(ContextCompat
                                    .getDrawable(MainActivity.this,
                                            R.drawable.priority_low));
                } else {
                    // setup flag for priority
                   if(Todo.getPriority().equals("HIGH")){
                       viewHolder.priority
                               .setImageDrawable(ContextCompat
                                       .getDrawable(MainActivity.this,
                                               R.drawable.priority_high));
                   }else if(Todo.getPriority().equals("MEDIUM")){
                       viewHolder.priority
                               .setImageDrawable(ContextCompat
                                       .getDrawable(MainActivity.this,
                                               R.drawable.priority_normal));
                   }else{
                       viewHolder.priority
                               .setImageDrawable(ContextCompat
                                       .getDrawable(MainActivity.this,
                                               R.drawable.priority_low));
                   }
                }
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getSupportFragmentManager();
                        Todo clickedItem = mFirebaseAdapter.getItem(position);
                        EditItemFragment frag = new EditItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("todoItem",clickedItem);
                        bundle.putString("todoKey",mFirebaseAdapter.getRef(position).getKey());
                        frag.setArguments(bundle);
                        frag.show(fm, "fragment_edit_name");
                    }
                });
            }
        };
        mTodoRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTodoRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mTodoRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //onDataChange called so remove progress bar
                //make a call to dataSnapshot.hasChildren() and based
                //on returned value show/hide empty view
                //use helper method to add an Observer to RecyclerView

                if(!dataSnapshot.hasChildren()){
                    Toast.makeText(getApplicationContext(),"no data found.", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int TodoCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (TodoCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mTodoRecyclerView.scrollToPosition(positionStart);
                }
            }

        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int indexOfDeletedItem = viewHolder.getAdapterPosition();
                final Todo deletedItem = mFirebaseAdapter.getItem(indexOfDeletedItem);
                final DatabaseReference currRef = mFirebaseAdapter.getRef(indexOfDeletedItem);
                currRef.removeValue();
                Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Deleted Item", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"snack bar clicked",Toast.LENGTH_LONG).show();
                                currRef.setValue(deletedItem);
                            }
                        }).show();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mTodoRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.action_settings).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return false;
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = EditItemFragment.newInstance();
        editItemFragment.show(fm, "fragment_edit_name");
    }
}
