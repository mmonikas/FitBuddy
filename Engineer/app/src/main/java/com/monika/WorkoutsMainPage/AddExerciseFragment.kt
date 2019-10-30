import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.WorkoutsMainPage.WorkoutsListPresenter

class AddExerciseFragment : Fragment() {

//    val presenter = WorkoutsListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            val workouts = arguments?.get("workouts") as ArrayList<Workout>
//            presenter.workoutsList = workouts
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercise, container, false)
    }
}