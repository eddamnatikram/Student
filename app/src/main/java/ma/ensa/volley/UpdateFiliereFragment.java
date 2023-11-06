package ma.ensa.volley;

import android.os.Bundle;
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

public class UpdateFiliereFragment extends Fragment implements View.OnClickListener {

    private EditText code, libelle;
    private Button updateButton;
    RequestQueue requestQueue;
    private String updateUrl = "http://192.168.126.39:8083/api/v1/filieres/{id}"; // Remplacez {id} par l'ID de la filière à mettre à jour
    private long filiereId; // Ajoutez cette variable pour stocker l'ID de la filière

    public void setFiliereId(long id) {
        this.filiereId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_filiere, container, false);
        code = view.findViewById(R.id.code); // Remplacez R.id.code par l'ID de votre champ de texte pour le code de la filière
        libelle = view.findViewById(R.id.libelle); // Remplacez R.id.libelle par l'ID de votre champ de texte pour le libellé de la filière
        updateButton = view.findViewById(R.id.updateButton); // Remplacez R.id.updateButton par l'ID de votre bouton de mise à jour
        updateButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
        try {
            String inputCode = code.getText().toString();
            String inputLibelle = libelle.getText().toString();

            jsonBody.put("code", inputCode);
            jsonBody.put("libelle", inputLibelle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(requireContext());
        String url = updateUrl.replace("{id}", String.valueOf(filiereId));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url, jsonBody, new Response.Listener<JSONObject>()  {
            @Override
            public void onResponse(JSONObject response) {
                // Logique de gestion de la réponse de mise à jour de la filière
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Logique de gestion des erreurs de mise à jour de la filière
            }
        });
        requestQueue.add(request);
    }
}
