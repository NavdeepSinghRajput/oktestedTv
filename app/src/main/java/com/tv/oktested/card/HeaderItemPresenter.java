package com.tv.oktested.card;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import com.tv.oktested.R;
import com.tv.oktested.model.HeaderItemModel;

public class HeaderItemPresenter extends RowHeaderPresenter {

    private float mUnselectedAlpha;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.header_item_layout, null);
       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        HeaderItemModel iconHeaderItem = (HeaderItemModel) ((PageRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);

        ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);
        int iconResId = iconHeaderItem.getIconResId();
        if( iconResId != HeaderItemModel.ICON_NONE) { // Show icon only when it is set.
            Drawable icon = rootView.getResources().getDrawable(iconResId, null);
            iconView.setImageDrawable(icon);
        } /*  Drawable icon = rootView.getResources().getDrawable(R.drawable.ic_eye_white_24dp, null);
        iconView.setImageDrawable(icon);
*/
        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(iconHeaderItem.getName());

      /*  IconHeaderItem iconHeaderItem = (IconHeaderItem) ((ListRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);

        ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);
        int iconResId = iconHeaderItem.getIconResId();
        if( iconResId != IconHeaderItem.ICON_NONE) { // Show icon only when it is set.
            Drawable icon = rootView.getResources().getDrawable(iconResId, null);
            iconView.setImageDrawable(icon);
        }

        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(iconHeaderItem.getName());*/
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: TEMP - remove me when leanback onCreateViewHolder no longer sets the mUnselectAlpha,AND
    // also assumes the xml inflation will return a RowHeaderView
  /*  @Override
    protected void onSelectLevelChanged(RowHeaderPresenter.ViewHolder holder) {
        // this is a temporary fix
        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
                (1.0f - mUnselectedAlpha));
    }*/

}