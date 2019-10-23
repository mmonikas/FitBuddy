package com.monika;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.monika.HomeScreen.HomeFragment;
import com.monika.HomeScreen.HomeFragmentPresenter;
import com.monika.Model.WorkoutComponents.Exercise;
import com.monika.Model.WorkoutPlan.Workout;

import java.util.ArrayList;


public class LoaderFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            ArrayList<Exercise> workoutArrayList = (ArrayList<Exercise>) getArguments().get("exercises");
            Navigation.findNavController(view).navigate(R.id.homeFragment, getArguments());
        }
    }
}