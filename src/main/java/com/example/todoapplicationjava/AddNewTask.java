package com.example.todoapplicationjava;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todoapplicationjava.Model.ToDoModel;
import com.example.todoapplicationjava.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG="ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    // changes
    private OnTaskAddedListener listener;
    public static AddNewTask newInstance(){
        return new AddNewTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container,false);
//        if(getDialog()!=null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newTaskText= getView().findViewById(R.id.newTaskText);
        newTaskSaveButton= getView().findViewById(R.id.newTaskButton);
        boolean isUpdate=false;

        final Bundle bundle=getArguments();
        if (bundle!=null){
            isUpdate=true;
            String task= bundle.getString("task");
            newTaskText.setText(task);
            assert task!=null;
            if (task.length()>0){
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }
        }

        db=new DatabaseHandler(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().isBlank()){
                newTaskSaveButton.setEnabled(false);
                newTaskSaveButton.setTextColor(Color.GRAY);
            } else if (s.toString().equals("Add Task")) {
                newTaskSaveButton.setEnabled(false);
                newTaskSaveButton.setTextColor(Color.GRAY);
            } else {
                newTaskSaveButton.setEnabled(true);
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimaryDark));
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String text=newTaskText.getText().toString();
                if (finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text);
                }else {
                    ToDoModel task=new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity=getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }

    public void setOnTaskListener(OnTaskAddedListener listener) {
        this.listener=listener;
    }
}
