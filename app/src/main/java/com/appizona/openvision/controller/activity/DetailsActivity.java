package com.appizona.openvision.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appizona.openvision.R;
import com.appizona.openvision.database.TodoDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.due_date)
    TextView dueDate;
    @BindView(R.id.done_btn)
    Button doneBtn;
    @BindView(R.id.detail_root)
    LinearLayout detailRoot;

    private String timeStamp;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        initToolbar();
        setupListeners();
        if (getIntent().hasExtra("title")) {
            isEditable = true;
            fillData();
        }

    }

    private void fillData() {

        doneBtn.setText("Update");

        Bundle bundle = getIntent().getExtras();

        String titlee = bundle.getString("title");
        String descriptionn = bundle.getString("description");
        String dueDatee = bundle.getString("due_date");

        title.setText(titlee);
        description.setText(descriptionn);
        dueDate.setText(dueDatee);


        timeStamp = bundle.getString("time_stamp");

        enableDisableRoot(detailRoot, false);

    }

    protected static void enableDisableRoot(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableRoot(group.getChildAt(idx), enabled);
            }
        }
    }

    private void setupListeners() {
        doneBtn.setOnClickListener(this);
        dueDate.setOnClickListener(this);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEditable) {
            getMenuInflater().inflate(R.menu.menu_details, menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_todo:
                enableDisableRoot(detailRoot, true);
                return true;

            case R.id.delete_todo:
                if (TodoDatabase.getInstance(this).deleteTodo(timeStamp)) {
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isDataValid(EditText... data) {
        for (EditText checkET : data) {
            if (TextUtils.isEmpty(checkET.getText().toString().trim())) {
                checkET.setError("This field is required!");
                checkET.setFocusableInTouchMode(true);
                checkET.requestFocus();
                return false;
            } else {
                checkET.setError(null);
            }
        }
        return true;
    }

    protected boolean isDataValid(TextView... data) {
        for (TextView checkET : data) {
            if (TextUtils.isEmpty(checkET.getText().toString().trim())) {
                checkET.setError("This field is required!");
                checkET.setFocusableInTouchMode(true);
                checkET.requestFocus();
                return false;
            } else {
                checkET.setError(null);
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done_btn:

                if (isDataValid(title, description) && isDataValid(dueDate)) {
                    if (doneBtn.getText().equals("Update")) {
                        updateTodo(title.getText().toString(), dueDate.getText().toString(), description.getText().toString(), "false");
                    } else {
                        insertTodoToDatabase();
                    }

                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;

            case R.id.due_date:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DetailsActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
        }
    }

    private void updateTodo(String title, String dueDate, String description, String isDone) {

        TodoDatabase.getInstance(this).updateTodo(title, dueDate, timeStamp, description, isDone);
    }

    private void insertTodoToDatabase() {
        String todoTitle = title.getText().toString();
        String todoDescription = description.getText().toString();
        String todoDueDate = dueDate.getText().toString();
        String todoTimeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());

        TodoDatabase.getInstance(this).addTodo(todoTitle, todoDueDate, todoTimeStamp, todoDescription, "false");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dueDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
    }
}
