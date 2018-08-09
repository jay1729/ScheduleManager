package com.gvjay.schedulemanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private Date currentDate;
    private int granularity = 0;
    private String[] DAY_VALUES;
    private Calendar calendar;
    private int offset = 0;
    private int CURRENT_POSITION = Integer.MAX_VALUE/2;

    public CalendarFragment(){
        calendar = Calendar.getInstance();
        currentDate = Calendar.getInstance().getTime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        granularity = bundle.getInt("granularity");
        offset = bundle.getInt("offset");
        Log.i("Current Date Value", currentDate.toString());
        Log.i("Granul", ""+granularity);
        Log.i("offset", ""+offset);

        View view;

        switch (granularity){
            case 0:
                view = inflater.inflate(R.layout.day_fragment, container, false);
                TextView dayValue = view.findViewById(R.id.day_value);
                TextView dateValue = view.findViewById(R.id.date_value);

                DAY_VALUES = container.getResources().getStringArray(R.array.day_values);

                long dayOffset = offset;
                dayOffset *=86400000;
                calendar.setTimeInMillis(currentDate.getTime()+dayOffset);

                dayValue.setText(DAY_VALUES[calendar.get(Calendar.DAY_OF_WEEK)-1]);
                dateValue.setText(calendar.getTime().toString().substring(4,10));
                break;

            default:
                view = super.onCreateView(inflater, container, savedInstanceState);
        }

        return view;
    }
}
