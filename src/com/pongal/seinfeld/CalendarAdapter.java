package com.pongal.seinfeld;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.pongal.seinfeld.DateState.Status;
import com.pongal.seinfeld.data.Date;
import com.pongal.seinfeld.data.Task;

public class CalendarAdapter extends BaseAdapter {

    Date date;
    Date startDate;
    Set<Date> disabledDates = new HashSet<Date>();
    private int count;
    String[] headers = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    private Set<CalendarSelectHandler> selectHandler = new HashSet<CalendarSelectHandler>();
    GridView view;
    Task task;

    public CalendarAdapter(GridView view) {
	this.view = view;
	this.view.setAdapter(this);
    }

    private void initializeMonth(Date theDate) {
	date = theDate;
	date.resetToFirstDayOfMonth();
	startDate = date.clone();
	int startDayOfWeek = date.getDayOfWeek();
	startDate.addDays(1 - startDayOfWeek);
	int totalDays = date.getMaximumDays();
	Double noOfRows = Math.ceil(((startDayOfWeek - 1 + totalDays) / 7.0));
	count = noOfRows.intValue() * 7;
    }

    void setData(Task task) {
	this.task = task;
	initializeMonth(new Date());
    }

    public void disableDates(Set<Date> dates) {
	disabledDates = dates;
    }

    public void addSelectHandler(CalendarSelectHandler handler) {
	selectHandler.add(handler);
    }

    @Override
    public int getCount() {
	return count + headers.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {	
	Context context = parent.getContext();
	TextView textView = (TextView) convertView;
	if (position < headers.length) {
	    return textView == null ? createDayCell(context, position) : textView;
	} else {
	    DateState model;
	    if(textView == null) {
		textView = createDateCell(context);
		model = getModel(position);
		textView.setTag(model);
	    } else {
		model = (DateState)textView.getTag();
	    }
	    textView.setText(Integer.toString(model.getDate().getDay()));
	    refreshStyle(textView);
	    Log.d(null, "position : " + position + " model : " + model);
	    return textView;
	}
    }

    private TextView createDateCell(Context context) {
	TextView textView = new TextView(context);
	textView.setLayoutParams(new GridView.LayoutParams(-1, Util.getInDIP(36, context)));
	textView.setGravity(Gravity.CENTER);
	textView.setOnClickListener(getDateClickListener());
	return textView;
    }

    private TextView createDayCell(Context context, int index) {
	TextView view = new TextView(context);
	view.setLayoutParams(new GridView.LayoutParams(-1, Util.getInDIP(36, context)));
	view.setGravity(Gravity.CENTER);
	view.setText(headers[index]);
	view.setBackgroundResource(R.color.calHeaderBg);
	view.setTextAppearance(view.getContext(), R.style.calHeader);
	view.setEnabled(false);
	return view;
    }

    void refreshStyle(TextView view) {
	Context context = view.getContext();
	DateState model = ((DateState) view.getTag());
	switch (model.getStatus()) {
	case DISABLED:
	    view.setTextAppearance(context, R.style.calFieldDisabled);
	    view.setBackgroundResource(R.layout.datebg);
	    view.setEnabled(false);
	    break;
	case NORMAL:
	    view.setTextAppearance(context, R.style.calField);
	    view.setBackgroundResource(R.layout.datebg);
	    view.setEnabled(true);
	    break;
	case SELECTED:
	    view.setTextAppearance(context, R.style.calFieldSelected);
	    view.setBackgroundResource(R.layout.datebg_highlight);
	    view.setEnabled(true);
	}
    }

    private OnClickListener getDateClickListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(View view) {
		DateState data = (DateState) view.getTag();
		data.toggleSelected();
		refreshStyle((TextView) view);
		notifySelectHandlers(data);
	    }
	};
    }

    private DateState getModel(int index) {
	index = index - headers.length;
	Date temp = startDate.clone();
	temp.addDays(index);
	DateState model = new DateState(temp);
	boolean sameMonth = temp.getMonth() == date.getMonth();
	boolean disabledDate = disabledDates.contains(temp);
	if (!sameMonth || disabledDate) {
	    model.setStatus(Status.DISABLED);
	} else if (task.isAccomplishedDate(temp)) {
	    model.setStatus(Status.SELECTED);
	}
	return model;
    }

    private void notifySelectHandlers(DateState state) {
	for (CalendarSelectHandler h : selectHandler) {
	    h.onChange(state);
	}
    }

    @Override
    public Object getItem(int arg0) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return 0;
    }
}
