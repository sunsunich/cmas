package org.cmas.activities.social;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import org.cmas.entities.User;
import org.cmas.mobile.R;
import org.cmas.util.android.BaseEfficientAdapter;

import java.util.List;

public class UserSearchAdapter<T extends User> extends BaseEfficientAdapter<T, UserSearchViewHolder> {

    public UserSearchAdapter(List<T> data, Context activity) {
        super(data, activity, R.layout.user_search_row);

    }

    @Override
    protected UserSearchViewHolder createHolder(View view) {
        return new UserSearchViewHolder(
                (TextView) view.findViewById(R.id.name)
        );
    }

    @Override
    protected void modifyHolder(UserSearchViewHolder holder, T datum) {
        holder.getName().setText(datum.getFirstName() + ' ' + datum.getLastName());

    }
}
