package com.appizona.openvision.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appizona.openvision.R;
import com.appizona.openvision.adapter.TodoAdapter;
import com.appizona.openvision.callback.OnTodoClickListener;
import com.appizona.openvision.database.TodoDatabase;
import com.appizona.openvision.model.Todo;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 7;

    @BindView(R.id.todo_recycler)
    RecyclerView todoRecycler;
    @BindView(R.id.add_todo_fab)
    FloatingActionButton addTodoFab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.no_items_TV)
    TextView noItemsTV;

    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpToolBar();
        init();
        setupListeners();
    }

    private void setupListeners() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Todo> list = TodoDatabase.getInstance(MainActivity.this).getAllTodosByDate(query);
                if (list.size() == 0) {
                    noItemsTV.setVisibility(View.VISIBLE);
                    todoRecycler.setVisibility(View.GONE);
                } else {
                    noItemsTV.setVisibility(View.GONE);
                    todoRecycler.setVisibility(View.VISIBLE);
                    adapter.updateTodo(list);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                noItemsTV.setVisibility(View.GONE);
                todoRecycler.setVisibility(View.VISIBLE);
                adapter.updateTodo(getTodosFromDatabase());
            }
        });
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo App");
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        adapter = new TodoAdapter(this, getTodosFromDatabase());
        todoRecycler.setLayoutManager(mLayoutManager);
        todoRecycler.setAdapter(adapter);
        addTodoFab.setOnClickListener(this);


        if (getTodosFromDatabase().size() == 0) {
            noItemsTV.setVisibility(View.VISIBLE);
        } else {
            noItemsTV.setVisibility(View.GONE);

        }

        adapter.setOnTodoClickListener(new OnTodoClickListener() {
            @Override
            public void onTodoClicked(Todo todo) {
                startActivityForResult(new Intent(MainActivity.this, DetailsActivity.class)
                        .putExtra("title", todo.getTitle())
                        .putExtra("time_stamp", todo.getTimeStamp())
                        .putExtra("description", todo.getDescription())
                        .putExtra("due_date", todo.getDueDate()), REQUEST_CODE);
            }
        });

    }

    private List<Todo> getTodosFromDatabase() {
        return TodoDatabase.getInstance(this).getAllTodos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;

    }

//    private List<Todo> getDummyList() {
//
//        List<Todo> todos = new ArrayList<>();
//
//        Todo todo = new Todo();
//        todo.setTitle("Test1 Todo")
//                .setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()))
//                .setDone("true")
//                .setDueDate("10/08/1995")
//                .setDescription("Y5rbetak ya kef w y5rbet m3rftaaaaaaaaaaaaaaak sa7e7 dmmak 5afef bs ya retny m 3rftak :( ");
//
//        Todo todo1 = new Todo();
//        todo1.setTitle("Test2 Todo")
//                .setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()))
//                .setDone("false")
//                .setDueDate("10/08/2000")
//                .setDescription("Yehiaaaaaaaaaaaaaa");
//
//
//        todos.add(todo);
//        todos.add(todo1);
//        todos.add(todo1);
//        todos.add(todo);
//        todos.add(todo1);
//        todos.add(todo);
//        todos.add(todo);
//        todos.add(todo);
//        todos.add(todo);
//        todos.add(todo1);
//        todos.add(todo);
//        todos.add(todo1);
//        todos.add(todo);
//        todos.add(todo);
//        todos.add(todo1);
//        todos.add(todo);
//        return todos;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_todo_fab:
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            List<Todo> list = TodoDatabase.getInstance(this).getAllTodos();
            adapter.updateTodo(list);
            if (list.size() == 0) {
                noItemsTV.setVisibility(View.VISIBLE);
                todoRecycler.setVisibility(View.GONE);
            } else {
                noItemsTV.setVisibility(View.GONE);
                todoRecycler.setVisibility(View.VISIBLE);
            }
        }
    }
}
