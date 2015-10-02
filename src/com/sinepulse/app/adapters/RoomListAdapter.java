package com.sinepulse.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinepulse.app.R;
import com.sinepulse.app.entities.Room;
import com.sinepulse.app.utils.CommonTask;

public class RoomListAdapter extends ArrayAdapter<Room> {

	// protected static final String LOG_TAG =
	// RoomListAdapter.class.getSimpleName();

	Room roomIndex;
	ArrayList<Room> roomInquieryList;
	int _position = -1;
	int width;
	View oldView = null;
	private int layoutResourceId;
	private Context context;

	public RoomListAdapter(Context roomManagerFragment, int layoutResourceId,
			ArrayList<Room> roomList) {
		super(roomManagerFragment, layoutResourceId, new ArrayList<Room>());
		 addAll(roomList);
		this.layoutResourceId = layoutResourceId;
		this.context = roomManagerFragment;
		this.roomInquieryList = roomList;
	}

	public static class RoomManagerHolder {
		// public Room roomManagerEntity;
//		private ImageView ivListItemImage;
//		private ImageView ivListItemImageRight;
		private RelativeLayout rlItemDetails;
		private TextView room_name;
		private int rowID;

		private RoomManagerHolder(ImageView ivListItemImage,
				RelativeLayout rlItemDetails, TextView room_name,
				ImageView ivListItemImageRight, int rowID) {
//			this.ivListItemImage = ivListItemImage;
			this.rlItemDetails = rlItemDetails;
			this.room_name = room_name;
//			this.ivListItemImageRight = ivListItemImageRight;
			this.rowID = rowID;
		}

		public static RoomManagerHolder create(LinearLayout roomlistRowItem,
				int rowId) {
			ImageView ivListItemImage = (ImageView) roomlistRowItem
					.findViewById(R.id.ivListItemImage);
			RelativeLayout rlItemDetails = (RelativeLayout) roomlistRowItem
					.findViewById(R.id.rlItemDetails);
			TextView room_name = (TextView) roomlistRowItem
					.findViewById(R.id.room_name);
			ImageView ivListItemImageRight = (ImageView) roomlistRowItem
					.findViewById(R.id.ivListItemImageRight);
			return new RoomManagerHolder(ivListItemImage, rlItemDetails,
					room_name, ivListItemImageRight, rowId);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final RoomManagerHolder vh;

		roomIndex = roomInquieryList.get(position);
//		roomIndex.Id = position;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
			vh = RoomManagerHolder.create((LinearLayout) convertView,
					roomIndex.Id);
			convertView.setTag(vh);
			convertView.setPadding(4, 3, 4, 3);
			vh.rlItemDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);

			vh.room_name = (TextView) convertView.findViewById(R.id.room_name);

		} else {
			vh = (RoomManagerHolder) convertView.getTag();
		}
		vh.rowID = position;
//		convertView.setBackgroundColor(Color.TRANSPARENT);
		vh.room_name.setText(roomIndex.getName());

		if (_position != position) {
			/*
			 * if(position%2==0){
			 * convertView.setBackgroundResource(R.drawable.bg_cell_light);
			 * }else{
			 * convertView.setBackgroundResource(R.drawable.bg_cell_dark); }
			 */
			convertView.setBackgroundResource(R.drawable.bg_cell_lightx);
		}

		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	public int getSize() {
		return roomInquieryList.size();
	}

	public Room getItemAtPosition(int position) {
		roomIndex = roomInquieryList.get(position);
		return roomIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomInquieryList.size();
	}

	public void setSelection(int pos) {
		_position = pos;
	}
boolean touchEnabled = true;
	
	public void setTouchEnabled(boolean touchEnabled) {
		this.touchEnabled = touchEnabled;
	}

	RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.drawable.bg_cell_lightx);
				}
				setSelection(((RoomManagerHolder) v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};

}
