package com.example.todoapplicationjava;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplicationjava.Adapter.ToDoAdapter;
import com.example.todoapplicationjava.Model.ToDoModel;
import com.example.todoapplicationjava.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter taskAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;

    private DatabaseHandler db;

    EditText editTextTask;
    Button buttonAdd;
    ListView listViewTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        db=new DatabaseHandler(this);
        db.openDatabase();

        taskList=new ArrayList<>();
        tasksRecyclerView=findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter=new ToDoAdapter(db,this);
        tasksRecyclerView.setAdapter(taskAdapter);

        fab=findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}