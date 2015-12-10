package com.sinepulse.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sinepulse.app.R;
import com.sinepulse.app.utils.NavDrawerItem;


/**
 * use for loading navigation drawer items and their related sub items in
 * Home screen 
 * @author tac
 */

public class NavDrawerListAdapter extends BaseExpandableListAdapter implements
		OnGroupClickListener {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public static TextView basketCounterTv = null;
	public static ImageView expandIndicator = null;
	public static ImageView groupExpandIndicator = null;
//	private AsyncNavDrawerExpandFirstStepItemsLoading asyncNavDrawerExpandItemsLoading = null;
//	private OnChildClickListener secondLevelChildListener;
	public ProgressBar nav_drawer_progress_bar;
	public ProgressBar nav_drawer_family_progress_bar;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems,
			ArrayList<Object> childItem,
			OnChildClickListener secondLevelListener) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		this.Childtem = childItem;
	}

	int selectedIndex = -1;

	public void setSelectedPosition(int newSelection) {
		this.selectedIndex = newSelection;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	DisplayMetrics metrics;
	int width;

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final CustExpListview secondLevelexplv = new CustExpListview(context);

		secondLevelexplv.setOnGroupClickListener(this);
		// The below code is for smooth scrolling of expandable list
		secondLevelexplv.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}

		});

		secondLevelexplv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
//				secondLevelexplv.collapseGroup(groupPosition);
//
//				secondLevelChildListener.onChildClick(parent, v, groupPosition,
//						childPosition, id);

				return true;
			}
		});


//		familyAdapter = new SecondLevelAdapter();
//		secondLevelexplv.setAdapter(familyAdapter);
//		secondLevelexplv.setGroupIndicator(null);
		return secondLevelexplv;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		// here we are deciding which drawer item will expand for first list
//		if (groupPosition == 4) {
//			if (Home.mDrawerLayout.isDrawerOpen(groupPosition)) {
//				Home.mDrawerLayout.openDrawer(groupPosition);
//			}
//			return 1;
//		} else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getGroupCount() {
		//Teh size of the very first List
		return navDrawerItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// Here we are inflating the very first list layout
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
//		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
//		expandIndicator = (ImageView) convertView
//				.findViewById(R.id.expandIndicator);

		imgIcon.setImageResource(navDrawerItems.get(groupPosition).getIcon());
		txtTitle.setText(navDrawerItems.get(groupPosition).getTitle());
		
		
        //Here we are setting the touch effect in list
		if (groupPosition != selectedIndex) {
			convertView.setBackgroundResource(R.drawable.bg_cell_light);
		} else {
			convertView.setBackgroundResource(R.drawable.list_pressed);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	// TODO CAN WE SEPERATE THIS CLASS

	public class CustExpListview extends ExpandableListView {

		public CustExpListview(Context context) {
			super(context);

		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
					MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		}

	}


	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		 if(groupPosition==6){
			 nav_drawer_progress_bar.setVisibility(v.VISIBLE);
		 }else{
			 nav_drawer_progress_bar.setVisibility(v.INVISIBLE);
		 }
		return false;
	}

//	public void startProgress() {
//		if (nav_drawer_group_progress_bar != null) {
//			nav_drawer_group_progress_bar.setVisibility(View.VISIBLE);
//		}
//	}
//
//	public void stopProgress() {
//		if (nav_drawer_group_progress_bar != null) {
//			nav_drawer_group_progress_bar.setVisibility(View.INVISIBLE);
//		}
//	}


}
