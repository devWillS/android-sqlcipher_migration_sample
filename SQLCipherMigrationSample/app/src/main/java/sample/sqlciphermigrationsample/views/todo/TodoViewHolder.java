package sample.sqlciphermigrationsample.views.todo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sample.sqlciphermigrationsample.R;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView limit;
    private LinearLayout linear;

    public TodoViewHolder(View itemView) {
        super(itemView);
        linear = (LinearLayout) itemView.findViewById(R.id.linear);
        title = (TextView) itemView.findViewById(R.id.rowTitleTextView);
        limit = (TextView) itemView.findViewById(R.id.rowLimitTextView);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getLimit() {
        return limit;
    }

    public LinearLayout getLinear() {
        return linear;
    }
}
