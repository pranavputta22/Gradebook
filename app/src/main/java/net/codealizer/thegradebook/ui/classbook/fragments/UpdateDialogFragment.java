package net.codealizer.thegradebook.ui.classbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.StudentNotification;
import net.codealizer.thegradebook.ui.adapters.UpdatesPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Pranav on 10/16/16.
 */

public class UpdateDialogFragment extends DialogFragment {

    public static final String KEY_ACTIVITIES = "net.codealizer.thegradebook.ui.fragments.UpdateDialogFragment.KEY_ACTIVITIES";
    private UpdatesPagerAdapter pagerAdapter;

    private ViewPager viewPager;
    private Button next;
    private Button back;
    private Button cancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_updates, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.updates_pager);
        next = (Button) view.findViewById(R.id.updates_next);
        back = (Button) view.findViewById(R.id.updates_back);
        cancel = (Button) view.findViewById(R.id.updates_cancel);


        final ArrayList<StudentNotification> activities = (ArrayList<StudentNotification>) getArguments().getSerializable(KEY_ACTIVITIES);

        pagerAdapter = new UpdatesPagerAdapter(getChildFragmentManager(), activities);
        viewPager.setAdapter(pagerAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < activities.size()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    back.setEnabled(true);
                    back.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                    if (viewPager.getCurrentItem() == activities.size() - 1) {
                        next.setEnabled(false);
                        next.setTextColor(getActivity().getResources().getColor(R.color.md_grey_600));
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    next.setEnabled(true);
                    next.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                    if (viewPager.getCurrentItem() == 0) {
                        back.setEnabled(false);
                        back.setTextColor(getActivity().getResources().getColor(R.color.md_grey_600));
                    }
                }
            }
        });

        setCancelable(false);

        return view;
    }
}
