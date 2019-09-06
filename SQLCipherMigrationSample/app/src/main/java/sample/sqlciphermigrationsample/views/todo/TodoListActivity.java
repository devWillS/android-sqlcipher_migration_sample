package sample.sqlciphermigrationsample.views.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sample.sqlciphermigrationsample.R;
import sample.sqlciphermigrationsample.common.DividerItemDecoration;
import sample.sqlciphermigrationsample.models.db.DatabaseHandler;
import sample.sqlciphermigrationsample.models.db.DatabaseHelper;
import sample.sqlciphermigrationsample.models.db.FeedEntry;
import sample.sqlciphermigrationsample.models.db.TodoData;
import sample.sqlciphermigrationsample.views.delete.DeleteDialogFragment;
import sample.sqlciphermigrationsample.views.input.InputActivity;

public class TodoListActivity extends AppCompatActivity implements TodoRecyclerViewAdapter.TodoAdapterListener, DeleteDialogFragment.DeleteDialogListener {
    private TodoRecyclerViewAdapter adapter;
    private int position;
    private List<TodoData> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);


        RecyclerView rv = (RecyclerView) findViewById(R.id.todoListView);
        adapter = new TodoRecyclerViewAdapter();
        adapter.setListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.addItemDecoration(new DividerItemDecoration(this));
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), InputActivity.class);
                intent.putExtra("status", "newTodo");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }


    private void reloadList() {
        todoList = new ArrayList<>();
        //rawQueryメソッドでデータを取得
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase(getString(R.string.db_pass));
        todoList = DatabaseHandler.select(db);


        adapter.setTodoList(todoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void selectedTodo(TodoData todo) {
        Intent intent = new Intent(getApplication(), InputActivity.class);
        intent.putExtra("status", "updateTodo");
        intent.putExtra("todo", todo);
        startActivity(intent);
    }

    @Override
    public void onLongClicked(int position) {
        this.position = position;
        DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(getFragmentManager(), "delete");
    }

    @Override
    public void onClickOk() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_DELETE_FLG, 1);

        String whereArgs[] = {String.valueOf(todoList.get(position).getTodoID())};

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase(getString(R.string.db_pass));
        int ret;
        ret = DatabaseHandler.delete(db, todoList.get(position).getTodoID());

        reloadList();

        if (ret == -1) {
            Toast.makeText(this, "Delete失敗", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Delete成功", Toast.LENGTH_SHORT).show();
        }
    }
}