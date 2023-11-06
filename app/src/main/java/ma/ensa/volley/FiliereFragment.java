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
import ma.ensa.volley.beans.Filiere;


public class FiliereFragment extends Fragment {

    private Button addButton, updateButton, deleteButton;
    private List<Filiere> filiereList;
    private RecyclerView recyclerView;
    private static final String TAG = "FiliereFragment";
    private String insertUrl = "http://192.168.126.39:8083/api/v1/filieres";
    private FiliereAdapter adapter;
    public FiliereFragment() {
        // Required empty public constructor
    }

    public static FiliereFragment newInstance() {
        return new FiliereFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filiere, container, false);


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.filiereRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        filiereList = new ArrayList<>();
        adapter = new FiliereAdapter(filiereList, requireContext());
        recyclerView.setAdapter(adapter);

        // Fetch data from the API
        fetchFiliereData();
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Fragment addFiliereFragment = new AddFiliereFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, addFiliereFragment);
            fragmentTransaction.addToBackStack(null); // Ajoutez la transaction à la pile arrière pour permettre le retour
            fragmentTransaction.commit();
        });

        // Récupérer les boutons updateButton et deleteButton


        return view;
    }


    private void fetchFiliereData() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl,
                response -> {
                    Log.d(TAG, "Raw JSON Response: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String code = jsonObject.getString("code");
                            String libelle = jsonObject.getString("libelle");
                            Filiere filiere = new Filiere(id, code, libelle);
                            filiereList.add(filiere);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error converting response to JSON array: " + e.getMessage());
                    }
                },
                error -> {
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            // Show timeout error message to the user
                            Log.e(TAG, "TimeoutError: " + error.toString());
                        }
                    } else {
                        // Handle other network errors here
                        Log.e(TAG, "Error loading filieres: " + error.toString());
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

}
