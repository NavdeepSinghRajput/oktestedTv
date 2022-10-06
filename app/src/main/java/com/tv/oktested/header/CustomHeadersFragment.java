package com.tv.oktested.header;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.leanback.widget.VerticalGridView;

import com.tv.oktested.R;
import com.tv.oktested.app.page.FavouriteFragment;
import com.tv.oktested.app.page.HomeFragment;
import com.tv.oktested.app.page.SettingRowFragment;
import com.tv.oktested.model.HeaderItemModel;
import com.tv.oktested.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * Created by Sebastiano Gottardo on 08/11/14.
 */
public class CustomHeadersFragment extends HeadersFragment {

	private ArrayObjectAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHeaderAdapter();
//		customSetBackground(R.color.fastlane_background);
		setCustomPadding();
		/**
		 * The setOnItemSelectedListener has been not only deprecated, but brutally removed by
		 * Google. To get around this limitation, I went to see how BaseRowFragment handled it.
		 * Turns out it sets a listener to the GridView (which is a RecyclerView): there you go.
		 */
		VerticalGridView gridView = ((MainHeaderActivity) getActivity()).getVerticalGridView(this);
		gridView.setOnChildSelectedListener(new OnChildSelectedListener() {
			@Override
			public void onChildSelected(ViewGroup viewGroup, View view, int i, long l) {
				Object obj = ((ListRow) getAdapter().get(i)).getAdapter().get(0);
				getFragmentManager().beginTransaction().replace(R.id.rows_container, (Fragment) obj).commit();
				((MainHeaderActivity) getActivity()).updateCurrentFragment((Fragment) obj);
			}
		});

	}

	private void setHeaderAdapter() {
		adapter = new ArrayObjectAdapter();

		LinkedHashMap<Integer, Fragment> fragments = ((MainHeaderActivity) getActivity()).getFragments();

		int id = 0;
		for (int i = 0; i < fragments.size(); i++) {
			if(fragments.get(i) instanceof HomeFragment){
				HeaderItemModel header = new HeaderItemModel(1, "Home", R.drawable.ic_home_white_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}else if(fragments.get(i) instanceof CustomShowsFragment){
				HeaderItemModel header = new HeaderItemModel(2, "Shows", R.drawable.ic_eye_white_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}else if(fragments.get(i) instanceof CustomAnchorFragment){
				HeaderItemModel header = new HeaderItemModel(3, "Anchors", R.drawable.ic_star_white_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}else if(fragments.get(i) instanceof FavouriteFragment){
				HeaderItemModel header = new HeaderItemModel(4, "Favourite", R.drawable.ic_heart_white_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}else if(fragments.get(i) instanceof SettingRowFragment){
				HeaderItemModel header = new HeaderItemModel(5, "Account", R.drawable.ic_person_black_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}/*else{
				HeaderItemModel header = new HeaderItemModel(4, "Anchors"+i, R.drawable.ic_star_white_24dp);
				ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
				innerAdapter.add(fragments.get(i));
				adapter.add(id, new ListRow(header, innerAdapter));
				id++;
			}*/

		}

		setAdapter(adapter);
	}

	private void setCustomPadding() {
		getView().setPadding(0, Utils.convertDpToPixel(getActivity(), 128), Utils.convertDpToPixel(getActivity(), 48), 0);
		getView().setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.pink));
	}


//	private OnItemSelectedListener getDefaultItemSelectedListener() {
//		return new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(Object o, Row row) {
//				Object obj = ((ListRow) row).getAdapter().get(0);
//				getFragmentManager().beginTransaction().replace(R.id.rows_container, (Fragment) obj).commit();
//				((MainActivity) getActivity()).updateCurrentFragment((Fragment) obj);
//			}
//		};
//	}

	/**
	 * Since the original setBackgroundColor is private, we need to
	 * access it via reflection
	 *
	 * @param colorResource The colour resource
	 */

	private void customSetBackground(int colorResource) {
		try {
			Class clazz = HeadersFragment.class;
			Method m = clazz.getDeclaredMethod("setBackgroundColor", Integer.TYPE);
			m.setAccessible(true);
			m.invoke(this, getResources().getColor(colorResource));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
