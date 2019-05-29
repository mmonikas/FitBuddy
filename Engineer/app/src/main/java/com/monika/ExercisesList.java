package com.monika;

import java.util.ArrayList;
import java.util.List;

public class ExercisesList {

    List<Exercise> local_exercises;
    List<Exercise> all_exercises;
    int index;
    private static ExercisesList instance = new ExercisesList();

    private ExercisesList() {
        if (local_exercises == null) {
            local_exercises = new ArrayList<>();
            Exercise e1 = new Exercise(getLocalList().size(), "Squat");
            e1.setExercise_description("Perfect exercise to grow your quads.");
            e1.setCategory(Category.Legs);
            e1.setExercise_load("10");
            Exercise e2 = new Exercise(getLocalList().size(), "Bicycle crunch");
            e2.setExercise_description("Exercise targetting abs.");
            e2.setCategory(Category.ABS);
            e2.setExercise_load("5");

            addItemLocally(e1);
            addItemLocally(e2);

            index = local_exercises.size();

        }
        if(all_exercises == null) {
            all_exercises = new ArrayList<>();
        }

    }

    public static ExercisesList getInstance() {
        return instance;
    }

    public List<Exercise> getLocalList() {
        return local_exercises;
    }

    public Exercise getLocalItemAtPosition(int position) {
        return local_exercises.get(position);
    }

    public boolean addItemLocally(Exercise e) {

        if(e.isInputDataValid())
        {
            e.setExercise_id(getLocalList().size());
          //  DAOFactory.getExercises().insert(e);
            local_exercises.add(e);
            if (local_exercises.contains(e))
                return true;
            else
                return false;
        }
        else
            return false;

    }

    public void removeItemLocally(int position) {
        local_exercises.remove(position);
    }

    public void removeExerciseLocally(Exercise exercise) {
        local_exercises.remove(exercise);
    }


}
