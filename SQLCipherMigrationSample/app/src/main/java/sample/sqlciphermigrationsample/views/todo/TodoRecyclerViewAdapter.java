package sample.sqlciphermigrationsample.views.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sample.sqlciphermigrationsample.R;
import sample.sqlciphermigrationsample.models.db.TodoData;

class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private List<TodoData> todoList;


    interface TodoAdapterListener {
        void selectedTodo(TodoData todo);

        void onLongClicked(int position);
    }

    private TodoAdapterListener listener;

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new TodoViewHolder(inflate);
    }

    public void setTodoList(List<TodoData> todoList) {
        this.todoList = todoList;
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder holder, int position) {
        holder.getTitle().setText(todoList.get(position).getTitle());
        holder.getLimit().setText(todoList.get(position).getLimit());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                TodoData todo = todoList.get(position);
                if (listener != null) {
                    listener.selectedTodo(todo);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                if (listener != null) {
                    listener.onLongClicked(position);
                    return true;
                }
                return false;
            }
        });
    }

    void setListener(TodoAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

}
