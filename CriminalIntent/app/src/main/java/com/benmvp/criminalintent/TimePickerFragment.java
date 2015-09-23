package com.benmvp.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.benmvp.criminalintent.time";
    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date time = (Date) getArguments().getSerializable(ARG_TIME);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        View dialogTimeView = LayoutInflater.from(getActivity())
            .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) dialogTimeView.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
            .setView(dialogTimeView)
            .setTitle(R.string.time_picker_title)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendResult(
                        Activity.RESULT_OK,
                        new GregorianCalendar(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute(),
                            0
                        ).getTime()
                    );
                }
            })
            .create();
    }

    private void sendResult(int resultCode, Date time) {
        Fragment targetFragment = getTargetFragment();

        if (targetFragment == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        targetFragment.onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
