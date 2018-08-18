package com.gvjay.schedulemanager.EventDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gvjay.schedulemanager.Database.EventDBHandler;
import com.gvjay.schedulemanager.Database.ScheduledEvent;
import com.gvjay.schedulemanager.MainView.DayView;
import com.gvjay.schedulemanager.NotifyDataChanged;
import com.gvjay.schedulemanager.R;

public class DeleteEventDialog extends Dialog {

    ScheduledEvent scheduledEvent;
    TextView titleView;
    Button deleteButton;
    NotifyDataChanged notifyDataChanged;

    public DeleteEventDialog(@NonNull Context context, ScheduledEvent scheduledEvent, NotifyDataChanged ndc) {
        super(context);
        this.scheduledEvent = scheduledEvent;
        this.notifyDataChanged = ndc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_event_dialog);

        titleView = findViewById(R.id.ded_event_title);
        deleteButton = findViewById(R.id.ded_delete_button);

        titleView.setText(scheduledEvent.title);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDBHandler dbHandler = new EventDBHandler(view.getContext());
                int n = dbHandler.deleteEventById(scheduledEvent.ID);
                notifyDataChanged.notifyDataChanged();
                dismiss();
            }
        });
    }
}
