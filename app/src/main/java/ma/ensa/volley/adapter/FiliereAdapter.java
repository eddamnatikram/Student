package ma.ensa.volley.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import ma.ensa.volley.UpdateFiliereFragment;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.R;

public class FiliereAdapter extends RecyclerView.Adapter<FiliereAdapter.FiliereViewHolder> {
    private List<Filiere> filiereList;
    private Context context;
    private FiliereAdapter adapter;
    private String insertUrl = "http://192.168.126.39:8083/api/v1/filieres/{id}";
    public FiliereAdapter(List<Filiere> filiereList, Context context) {
        this.filiereList = filiereList;
        this.context = context;
    }

    @NonNull
    @Override
    public FiliereViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filiere, parent, false);
        return new FiliereViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiliereViewHolder holder, int position) {
        Filiere filiere = filiereList.get(position);
        holder.idTextView.setText(String.valueOf(filiere.getId()));
        holder.codeTextView.setText(filiere.getCode());
        holder.libelleTextView.setText(filiere.getLibelle());
        holder.updateButton.setOnClickListener(v -> {
            long filiereId = filiere.getId(); // Obtenez l'ID de la filière sélectionnée
            Fragment updateFiliereFragment = new UpdateFiliereFragment();
            Bundle args = new Bundle();
            args.putLong("filiereId", filiereId); // Passez l'ID de la filière au fragment de mise à jour
            updateFiliereFragment.setArguments(args);
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, updateFiliereFragment);
            fragmentTransaction.addToBackStack(null); // Ajoutez la transaction à la pile arrière pour permettre le retour
            fragmentTransaction.commit();
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous supprimer cette filière ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteFiliere(filiere.getId());
                            filiereList.remove(position); // Supprimer l'élément de la liste locale
                            notifyItemRemoved(position); // Notifier l'adaptateur de la suppression de l'élément à la position spécifiée
                            notifyItemRangeChanged(position, filiereList.size()); // Notifier l'adaptateur du changement de la plage d'éléments
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });


    }

    @Override
    public int getItemCount() {
        return filiereList.size();
    }

    static class FiliereViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, codeTextView, libelleTextView;
        Button updateButton, deleteButton;

        public FiliereViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            codeTextView = itemView.findViewById(R.id.codeTextView);
            libelleTextView = itemView.findViewById(R.id.libelleTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
    private void deleteFiliere(int id) {
        String deleteUrl = insertUrl.replace("{id}", String.valueOf(id));
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Afficher une alerte pour indiquer que la filière a été supprimée
                        Toast.makeText(context, "Filière supprimée", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérer les erreurs ici
                    }
                });

        // Ajout de la requête à la file d'attente de la requête
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
