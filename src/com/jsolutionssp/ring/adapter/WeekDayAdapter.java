package com.jsolutionssp.ring.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jsolutionssp.ring.R;
import com.jsolutionssp.ring.RingActivity;

public class WeekDayAdapter extends ArrayAdapter<String> {

	private String[] data;

	private Context context;

	private SharedPreferences.Editor editor;

	private SharedPreferences settings;

	private LinearLayout linearLayout;
	
	private RadioButton radioButton;

	public WeekDayAdapter(Context context, int itemInitialListview,
			String[] data) {
		super(context, itemInitialListview, data);

		this.context = context;
		this.data = data;
		settings = context.getSharedPreferences(RingActivity.PREFS_NAME, 0);
		editor = settings.edit();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		LinearLayout item = (LinearLayout) inflater.inflate(R.layout.item_initial_listview, null);
		item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout l = (LinearLayout) v;
				boolean day;
				if (position == 0)
					day = true;
				else
					day = false;

				editor.putBoolean("firstDayofWeek", day);
				editor.commit();
				RadioButton button = (RadioButton) l.getChildAt(1);

				if (l.isSelected()) {
					l.setSelected(false);
					button.setChecked(false);
				}
				else {
					if (linearLayout != null) {
						linearLayout.setSelected(false);
						radioButton.setChecked(false);
					}
					l.setSelected(true);
					button.setChecked(true);
				}
				linearLayout = l;
				radioButton = button;
			}
		});
		
		TextView selection = (TextView) item.findViewById(R.id.each_ring_text);
		selection.setGravity(Gravity.LEFT);
		String text = data[position];
		selection.setText(text);
		RadioButton button = (RadioButton) item.findViewById(R.id.each_ring_radiobutton);
		button.setGravity(Gravity.RIGHT);
		if (settings.getBoolean("firstDayofWeek", true) && position == 0)
			while (!item.performClick());
		else if (!settings.getBoolean("firstDayofWeek", true) && position == 1)
			while (!item.performClick());
			
		return(item);
	}
}
