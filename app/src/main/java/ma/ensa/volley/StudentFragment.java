package ma.ensa.volley;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.FiliereAdapter;
import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {
    private Button addButton;
    private List<Student> studentList;
    private RecyclerView recyclerView;
    private static final String TAG = "StudentFragment";
    private String insertUrl = "http://192.168.126.39:8083/api/student";
    private StudentAdapter adapter;
    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance() {
        return new StudentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.studentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList, requireContext());
        recyclerView.setAdapter(adapter);

        // Fetch data from the API
        fetchStudentData();
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Fragment addStudentFragment = new AddStudentFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, addStudentFragment);
            fragmentTransaction.addToBackStack(null); // Ajoutez la transaction à la pile arrière pour permettre le retour
            fragmentTransaction.commit();
        });

        // Récupérer les boutons updateButton et deleteButton


        return view;
    }


    private void fetchStudentData() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl,
                response -> {
                    Log.d(TAG, "Raw JSON Response: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Long id = jsonObject.getLong("id");
                            String name = jsonObject.getString("name");
                            int phone = jsonObject.getInt("phone");
                            String email = jsonObject.getString("email");
                            JSONObject filiereObject = jsonObject.getJSONObject("filiere");
                            int filiereId = filiereObject.getInt("id");
                            String code = filiereObject.getString("code");
                            String libelle = filiereObject.getString("libelle");
                            Filiere filiere = new Filiere(filiereId, code, libelle);
                            Student student = new Student(id, name, phone, email, filiere);
                            studentList.add(student);
                        }

                        adapter.notifyDataSetChanged(); // Notifiez l'adaptateur du changement de données
                    } catch (JSONException e) {
                        Log.e(TAG, "Error converting response to JSON array: " + e.getMessage());
                    }
                },
                error -> {
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            // Affichez un message d'erreur de délai d'attente à l'utilisateur
                            Log.e(TAG, "TimeoutError: " + error.toString());
                        }
                    } else {
                        // Gérer d'autres erreurs réseau ici
                        Log.e(TAG, "Error loading students: " + error.toString());
                    }
                }
        );

        requestQueue.add(stringRequest);
    }



}