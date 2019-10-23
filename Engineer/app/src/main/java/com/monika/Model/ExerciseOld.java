package com.monika.Model;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.monika.Enums.Category;


/**
 * Klasa służąca do przechowywania i walidacji danych związanych z ćwiczeniami.
 */
public class ExerciseOld {

    /**
     * ID ćwiczenia
     */
    private int exercise_id;
    /**
     * nazwa ćwiczenia
     */
    @NonNull
    private
    String exercise_name;
    /**
     * opis ćwiczenia
     */
    private String exercise_description;
    /**
     * sprzęt potrzebny do wykonania ćwiczenia
     */
    private String exercise_equipment;
    /**
     * sugerowany ciężar do użycia podczas wykonywania ćwiczenia
     */
    private String exercise_load;
    /**
     * poziom trudności ćwiczenia
     */
    private int exercise_level;
    /**
     * czy ćwiczenie należy do ulubionych
     */
    private boolean exercise_is_favorite;
    /**
     * obrazek demonstrujący wykonanie ćwiczenia
     */
    private Uri exercise_image;
    /**
     * kategoria, do której należy ćwiczenie
     */
    private Category category;


    public ExerciseOld(int exercise_id, @NonNull String exercise_name){
        this.exercise_id = exercise_id;
        this.exercise_name = exercise_name;
    }

    public ExerciseOld(int exercise_id, @NonNull String exercise_name, String exercise_description,
                       String exercise_equipment, String exercise_load, int exercise_level,
                       Category category, Uri exercise_image) {
        this.exercise_id = exercise_id;
        this.exercise_name = exercise_name;
        this.exercise_description = exercise_description;
        this.exercise_equipment = exercise_equipment;
        this.exercise_load = exercise_load;
        this.exercise_level = exercise_level;
        this.category = category;
        this.exercise_image = exercise_image;
    }

    @NonNull
    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getExercise_description() {
        return exercise_description;
    }

    public void setExercise_description(String exercise_description) {
        this.exercise_description = exercise_description;
    }

    public String getExercise_equipment() {
        return exercise_equipment;
    }

    public void setExercise_equipment(String exercise_equipment) {
        this.exercise_equipment = exercise_equipment;
    }

    public String getExercise_load() {
        return exercise_load;
    }

    public void setExercise_load(String exercise_load) {
        this.exercise_load = exercise_load;
    }

    public int getExercise_level() {
        return exercise_level;
    }

    public void setExercise_level(int exercise_level) {
        this.exercise_level = exercise_level;
    }

    public boolean isExercise_is_favorite() {
        return exercise_is_favorite;
    }

    public void setExercise_is_favorite(boolean exercise_is_favorite) {
        this.exercise_is_favorite = exercise_is_favorite;
    }

    public Uri getExercise_image() {
        return exercise_image;
    }

    public void setExercise_image(Uri image) {
        this.exercise_image = image;
    }

    public String getCategory() {
        return String.valueOf(category);
    }

    public Category getCategoryCategory() {return category;}

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     metoda walidująca czy, dane niezbędne do zapisania ćwiczenia są poprawne*
     @return funkcja zwraca wartość typu boolean (jeśli true, to dane są poprawne, false jeśli niepoprawne)
     */
    public boolean isInputDataValid() {
        String pattern = "\\s+";
        if(getExercise_name().length() == 0 || getExercise_name().matches(pattern)
                || getCategoryCategory() == null)
            return false;
        else
            return true;
    }
}
