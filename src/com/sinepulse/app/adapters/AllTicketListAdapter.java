/**
 * 
 */
package com.sinepulse.app.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinepulse.app.R;
import com.sinepulse.app.entities.Ticket;
import com.sinepulse.app.utils.CommonTask;

/**
 * @author tanvir.ahmed
 *
 */
public class AllTicketListAdapter extends ArrayAdapter<Ticket> {

	// protected static final String LOG_TAG =
	// RoomListAdapter.class.getSimpleName();

	Ticket ticketIndex;
	ArrayList<Ticket> ticketInquieryList;
	int _position = -1;
	int width;
	View oldView = null;
	private int layoutResourceId;
	private Context context;

	public AllTicketListAdapter(Context helpActivity, int layoutResourceId,
			ArrayList<Ticket> ticketList) {
		super(helpActivity, layoutResourceId, new ArrayList<Ticket>());
		addAll(ticketList);
		this.layoutResourceId = layoutResourceId;
		this.context = helpActivity;
		this.ticketInquieryList = ticketList;
	}

	public static class TicketHolder {
		// public Room roomManagerEntity;
		private RelativeLayout rlTicketDetails;
		private TextView tvTicketNumber;
		private TextView tvTktSubject;
		private TextView tvTktSubmissionDate;
		private TextView tvTktStatus;
		
		private int rowID;

		private TicketHolder(RelativeLayout rlTicketDetails, TextView tvTicketNumber,
				TextView tvTktSubject,TextView tvTktSubmissionDate,TextView tvTktStatus, int rowID) {
			this.tvTicketNumber = tvTicketNumber;
			this.rlTicketDetails = rlTicketDetails;
			this.tvTktSubject = tvTktSubject;
			this.tvTktSubmissionDate = tvTktSubmissionDate;
			this.tvTktStatus= tvTktStatus;
			this.rowID = rowID;
		}

		public static TicketHolder create(LinearLayout ticketlistRowItem,
				int rowId) {
			TextView tvTicketNumber = (TextView) ticketlistRowItem
					.findViewById(R.id.tvTicketNumber);
			RelativeLayout rlTicketDetails = (RelativeLayout) ticketlistRowItem
					.findViewById(R.id.rlTicketDetails);
			TextView tvTktSubject = (TextView) ticketlistRowItem
					.findViewById(R.id.tvTktSubject);
			TextView tvTktSubmissionDate = (TextView) ticketlistRowItem
					.findViewById(R.id.tvTktSubmissionDate);
			TextView tvTktStatus = (TextView) ticketlistRowItem
					.findViewById(R.id.tvTktStatus);
			return new TicketHolder(rlTicketDetails, tvTicketNumber,
					tvTktSubject, tvTktSubmissionDate,tvTktStatus, rowId);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final TicketHolder vh;

		ticketIndex = ticketInquieryList.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
			vh = TicketHolder.create((LinearLayout) convertView,
					ticketIndex.Id);
			convertView.setTag(vh);
			convertView.setPadding(4, 3, 4, 3);
			vh.rlTicketDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);

			vh.tvTicketNumber = (TextView) convertView.findViewById(R.id.tvTicketNumber);
			vh.tvTktSubject = (TextView) convertView.findViewById(R.id.tvTktSubject);
			vh.tvTktSubmissionDate = (TextView) convertView.findViewById(R.id.tvTktSubmissionDate);
			vh.tvTktStatus = (TextView) convertView.findViewById(R.id.tvTktStatus);

		} else {
			vh = (TicketHolder) convertView.getTag();
		}
		vh.rowID = position;
//		convertView.setBackgroundColor(Color.TRANSPARENT);
		vh.tvTicketNumber.setText(String.valueOf(ticketIndex.getId()));
		vh.tvTktSubject.setText(ticketIndex.getSubject());
		//parse the date and set in text view
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Long timeInMillis = Long.valueOf(ticketIndex.getSubmissionDate().getTime());
		Date LoggedAt = new Date(timeInMillis);
		vh.tvTktSubmissionDate.setText(formatter.format(LoggedAt));
		
		vh.tvTktStatus.setText(ticketIndex.getStatus());

		if (_position != position) {
			/*
			 * if(position%2==0){
			 * convertView.setBackgroundResource(R.drawable.bg_cell_light);
			 * }else{
			 * convertView.setBackgroundResource(R.drawable.bg_cell_dark); }
			 */
			convertView.setBackgroundResource(R.drawable.bgmedium);
		}

		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	public int getSize() {
		return ticketInquieryList.size();
	}

	public Ticket getItemAtPosition(int position) {
		ticketIndex = ticketInquieryList.get(position);
		return ticketIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ticketInquieryList.size();
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
					oldView.setBackgroundResource(R.drawable.bgmedium);
				}
				setSelection(((TicketHolder) v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};

}
