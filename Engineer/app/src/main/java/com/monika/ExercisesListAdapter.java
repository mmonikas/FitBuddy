package com.monika;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ExercisesListAdapter extends RecyclerView.Adapter<ExercisesListAdapter.MyViewHolder> {

    private List<Exercise> mDataset;
    private Context mContext;
    private Activity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExercisesListAdapter(Context mContext, List<Exercise> myDataset, Activity mActivity) {
        this.mDataset = myDataset;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExercisesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        final Exercise exercise = mDataset.get(position);
        viewHolder.eName.setText(exercise.getExercise_name());
        if(exercise.getExercise_description() != null)
            viewHolder.eDescription.setText(exercise.getExercise_description());
        else
            viewHolder.eDescription.setText("");

        viewHolder.eCategory.setText(exercise.getCategory());
        if(exercise.getExercise_image() == null) {
            Drawable defaultImg = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.exercises_menu_img, null);
            viewHolder.eImage.setImageDrawable(defaultImg);
        }

        else
            viewHolder.eImage.setImageURI(exercise.getExercise_image());

        viewHolder.deleteButton.setClickable(true);

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(exercise);
                ExercisesList.getInstance().removeExerciseLocally(exercise);
                notifyDataSetChanged();
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", true);
                bundle.putInt("id", position);
                bundle.putString("name", exercise.getExercise_name());
                bundle.putString("description", exercise.getExercise_description());
                bundle.putString("category", String.valueOf(exercise.getCategory()));
                bundle.putString("load", exercise.getExercise_load());
                bundle.putInt("level", exercise.getExercise_level());
                bundle.putString("imgpath", String.valueOf(exercise.getExercise_image()));

                Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.exerciseDetails, bundle);
            }
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView eName, eDescription, eCategory;
        ImageView eImage;
        Button editButton;
        Button deleteButton;

        MyViewHolder(View v) {
            super(v);
            eName = itemView.findViewById(R.id.card_exercise_name);
            eCategory = itemView.findViewById(R.id.card_exercise_category);
            eImage = itemView.findViewById(R.id.card_exercise_image);
            editButton = itemView.findViewById(R.id.open_exercise_edit);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }


}

