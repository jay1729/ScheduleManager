package com.gvjay.schedulemanager.EventDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gvjay.schedulemanager.Database.ScheduledEvent;
import com.gvjay.schedulemanager.R;

public class DescriptiveDialog extends Dialog implements View.OnClickListener {

    TextView eventTitle;
    ScheduledEvent scheduledEvent;
    Button ok;

    public DescriptiveDialog(@NonNull Context context, ScheduledEvent scheduledEvent) {
        super(context);
        this.scheduledEvent = scheduledEvent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_descriptive_dialog);

        eventTitle = findViewById(R.id.edd_event_title);
        eventTitle.setText(scheduledEvent.title);

        ok = findViewById(R.id.edd_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
