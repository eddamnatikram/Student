
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
        import com.android.volley.Response;
        import com.android.volley.TimeoutError;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

        import ma.ensa.volley.adapter.RoleAdapter;
        import ma.ensa.volley.beans.Role;

public class RoleFragment extends Fragment {

    private Button addButton;
    private List<Role> roleList;
    private RecyclerView recyclerView;
    private RoleAdapter adapter;
    private static final String TAG = "RoleFragment";
    private String fetchUrl = "http://192.168.126.39:8083/api/v1/roles"; // Modifier l'URL pour correspondre à votre API

    public RoleFragment() {
        // Required empty public constructor
    }

    public static RoleFragment newInstance() {
        return new RoleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.roleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        roleList = new ArrayList<>();
        adapter = new RoleAdapter(roleList, requireContext());
        recyclerView.setAdapter(adapter);

        // Fetch data from the API
        fetchRoleData();

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Fragment addRoleFragment = new AddRoleFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, addRoleFragment);
            fragmentTransaction.addToBackStack(null); // Ajoutez la transaction à la pile arrière pour permettre le retour
            fragmentTransaction.commit();
        });

        return view;
    }

    private void fetchRoleData() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fetchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Raw JSON Response: " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name"); // Vérifiez le nom exact de votre clé dans la réponse JSON
                                Role role = new Role((long) id, name);
                                roleList.add(role);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error converting response to JSON array: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message to the user
                                Log.e(TAG, "TimeoutError: " + error.toString());
                            }
                        } else {
                            // Handle other network errors here
                            Log.e(TAG, "Error loading roles: " + error.toString());
                        }
                    }
                }
        );

        requestQueue.add(stringRequest);
    }
}