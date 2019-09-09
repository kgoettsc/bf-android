package com.example.myapplication.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Venue;

public class VenueDialogFragment extends DialogFragment {
    Venue venue;

    public VenueDialogFragment(Venue venue) {
        this.venue = venue;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        StringBuilder sb = new StringBuilder(venue.getName());
        sb.append(" - ");

        for(String activity : venue.getActivities()) {
            if (venue.getActivities().indexOf(activity) != 0) {
                sb.append(", ");
            }

            sb.append(activity);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(sb.toString());

        return builder.create();
    }
}
