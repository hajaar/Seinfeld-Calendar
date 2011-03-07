package com.pongal.seinfeld.task;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pongal.seinfeld.R;
import com.pongal.seinfeld.data.Task;

public class EditTaskView extends Dialog {

    public static final int ADD_TASK = 1;
    public static final int EDIT_TASK = 2;

    Task task;
    TaskUpdatedHandler handler;
    EditText taskName;
    Button okButton;
    Button cancelButton;

    public EditTaskView(Context context) {
	super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.edittask);
	LayoutParams params = getWindow().getAttributes();
	params.width = LayoutParams.FILL_PARENT;
	setTitle("Add Task");
    }

    public void init(final Task task, int type, final TaskUpdatedHandler handler) {
	this.task = task;
	this.handler = handler;
	this.taskName = (EditText) findViewById(R.id.taskName);
	this.okButton = (Button) findViewById(R.id.addTask);
	this.cancelButton = (Button) findViewById(R.id.cancelTask);

	cancelButton.setOnClickListener(getCancelHandler());
	okButton.setOnClickListener(getSaveHandler());
	okButton.setText(type == ADD_TASK ? "Add" : "Edit");
	taskName.setText(task.getText());

//	taskName.requestFocus();
//	InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
//		Context.INPUT_METHOD_SERVICE);
//	inputMethodManager.showSoftInput(taskName, InputMethodManager.SHOW_FORCED);

	taskName.addTextChangedListener(new TextWatcher() {
	    public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
	    }

	    public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
	    }

	    public void afterTextChanged(Editable editable) {
		okButton.setEnabled(!(editable.length() == 0));
	    }
	});
    }

    private View.OnClickListener getCancelHandler() {
	return new View.OnClickListener() {
	    @Override
	    public void onClick(View view) {
		dismiss();
	    }
	};
    }

    private View.OnClickListener getSaveHandler() {
	return new View.OnClickListener() {
	    @Override
	    public void onClick(View view) {
		if (!taskName.getText().equals(task.getText())) {
		    Log.d(null, "Task id:" + task.getId());
		    task.setText(taskName.getText().toString());
		    handler.onUpdate(task);
		}
	    }
	};
    }

}