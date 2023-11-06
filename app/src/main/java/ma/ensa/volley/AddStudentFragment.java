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

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;

public class AddStudentFragment extends Fragment implements View.OnClickListener {
    private EditText id_user, name, username, phone, email, password, filiere_id, role_id;
    private Button bnAdd;
    RequestQueue requestQueue;

    String insertUrl = "http://192.168.126.39:8083/api/student";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);
        id_user = view.findViewById(R.id.id_user);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        filiere_id = view.findViewById(R.id.filiere_id);
        role_id = view.findViewById(R.id.role_id);

        bnAdd = view.findViewById(R.id.bnAdd);
        bnAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_user", Long.parseLong(id_user.getText().toString()));
            jsonBody.put("name", name.getText().toString());
            jsonBody.put("username", username.getText().toString());
            jsonBody.put("phone", Integer.parseInt(phone.getText().toString()));
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("password", password.getText().toString());

            JSONObject filiereObject = new JSONObject();
            filiereObject.put("id", Long.parseLong(filiere_id.getText().toString()));
            jsonBody.put("filiere", filiereObject);

             JSONObject roleObject = new JSONObject();
            roleObject.put("id", Long.parseLong(role_id.getText().toString()));
            jsonBody.put("role", roleObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response.toString());
                new AlertDialog.Builder(requireContext())
                        .setTitle("Succès")
                        .setMessage("student ajoutée avec succès")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
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
