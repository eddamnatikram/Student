package ma.ensa.volley;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class AddFiliereFragment extends Fragment implements View.OnClickListener {
    private EditText code, libelle;
    private Button bnAdd;
    RequestQueue requestQueue;

    String insertUrl = "http://192.168.126.39:8083/api/v1/filieres";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_filiere, container, false);
        code = view.findViewById(R.id.code);
        libelle = view.findViewById(R.id.libelle);
        bnAdd = view.findViewById(R.id.bnAdd);
        bnAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
        try {
            String inputCode = code.getText().toString();
            String inputLibelle = libelle.getText().toString();
            Log.d("MyApp", "Code: " + inputCode);
            Log.d("MyApp", "Libellé: " + inputLibelle);

            jsonBody.put("code", inputCode);
            jsonBody.put("libelle", inputLibelle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response.toString());
                // Afficher une alerte pour indiquer que la filière a été ajoutée avec succès
                new AlertDialog.Builder(requireContext())
                        .setTitle("Succès")
                        .setMessage("Filière ajoutée avec succès")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            // Revenir à FiliereFragment
                            getParentFragmentManager().popBackStack();
                        })
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }

}
