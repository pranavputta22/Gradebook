package net.codealizer.thegradebook.ui.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.apis.ic.classbook.PortalClassbook;
import net.codealizer.thegradebook.assets.BasicGradeDetail;
import net.codealizer.thegradebook.listeners.OnAssignmentEdittedListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Pranav on 10/10/16.
 */

public class GradesRecyclerViewAdapter extends RecyclerView.Adapter<GradesRecyclerViewAdapter.ViewHolder> {

    public ArrayList<ClassbookActivity> activities;
    Context mContext;
    private boolean isEBR;
    private ClassbookTask task;
    private PortalClassbook classbook;
    private OnAssignmentEdittedListener mListener;

    public GradesRecyclerViewAdapter(Context mContext, ArrayList<ClassbookActivity> activities,
                                     boolean ebr, OnAssignmentEdittedListener listener, ClassbookTask task,
                                     PortalClassbook classbook) {
        this.mContext = mContext;
        this.activities = activities;
        this.isEBR = ebr;
        this.mListener = listener;
        this.task = task;
        this.classbook = classbook;
    }

    @Override
    public GradesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_grades, parent, false);

        return new GradesRecyclerViewAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ClassbookActivity activity = activities.get(position);
        final Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);


        holder.dueDate.setText(activity.getDueDate());
        holder.title.setText(activity.name);

        final DecimalFormat format = new DecimalFormat("###.#");
        String score;
        try {
            score = format.format(Float.parseFloat(activity.score));
        } catch (Exception ex) {
            ex.printStackTrace();
            score = activity.score;
        }
        String totalPoints = format.format(activity.getTotalPoints());
        if (score == null) {
            holder.score.setText("N/A");
        } else if (isEBR || totalPoints == null || totalPoints.equals("null")) {
            holder.score.setText(score);
        } else {
            holder.score.setText(score + "/" + totalPoints);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String score;
                float s;

                try {
                    score = format.format(Float.parseFloat(activity.score));
                    s = Float.parseFloat(activity.score);
                } catch (Exception ex) {
                    score = "0";
                    s = 0;
                }

                v.vibrate(100);


                BasicGradeDetail detail = new BasicGradeDetail(activity.name, activity.getDueDate(), score, format.format((s / activity.getTotalPoints()) * 100),
                        format.format(activity.weight), activity.getComments(), isEBR, classbook);
                Alert.showProgressInformation(detail, mContext);
            }
        });

        Float s;

        try {
            s = Float.parseFloat(score);
        } catch (Exception e) {
            s = 0f;
        }

        if (!activity.isActive() || s == 0) {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.md_grey_400));
            holder.dueDate.setTextColor(mContext.getResources().getColor(R.color.md_grey_400));
            holder.score.setBackground(mContext.getResources().getDrawable(R.drawable.progress_counter_grey));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Alert.showGradeInactiveDialog(mContext);
                }
            });
        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.md_black_1000));
            holder.dueDate.setTextColor(mContext.getResources().getColor(R.color.md_grey_700));
            holder.score.setBackground(mContext.getResources().getDrawable(R.drawable.progress_counter_purple));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String score;
                    float s;

                    try {
                        score = format.format(Float.parseFloat(activity.score));
                        s = Float.parseFloat(activity.score);
                    } catch (Exception ex) {
                        score = "0";
                        s = 0;
                    }

                    v.vibrate(100);


                    BasicGradeDetail detail = new BasicGradeDetail(activity.name, activity.getDueDate(), score, format.format((s / activity.getTotalPoints()) * 100),
                            format.format(activity.weight), activity.getComments(), isEBR, classbook);
                    Alert.showProgressInformation(detail, mContext);
                }
            });
        }

        final float mScore = s;
        if (activity.custom) {
            holder.score.setBackground(mContext.getResources().getDrawable(R.drawable.progress_counter_red));
            holder.dueDate.setText("Custom Assignment");
            holder.dueDate.setTextColor(mContext.getResources().getColor(R.color.md_red_300));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    v.vibrate(100);

                    Alert.showEditAssignmentScoreDialog(task, activity.getBasicActivity(), mContext, mListener, activity.index, activity.masterIndex);
                }
            });
            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    v.vibrate(100);

                    BasicGradeDetail detail = new BasicGradeDetail(activity.name, activity.getDueDate(), activity.score,
                            format.format((mScore / activity.getTotalPoints()) * 100), String.valueOf(activity.weight), activity.getComments(), isEBR, classbook);
                    Alert.showProgressInformation(detail, mContext);

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void setActivities(ArrayList<ClassbookActivity> activities) {
        this.activities = activities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout card;
        TextView dueDate, title, score;

        public ViewHolder(View itemView) {
            super(itemView);

            dueDate = (TextView) itemView.findViewById(R.id.card_grade_due_date);
            title = (TextView) itemView.findViewById(R.id.card_grade_title);
            score = (TextView) itemView.findViewById(R.id.card_grade_score);

            card = (RelativeLayout) itemView.findViewById(R.id.card_grade);
        }
    }
}
