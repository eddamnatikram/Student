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

import ma.ensa.volley.UpdateRoleFragment; // Make sure to provide the correct name for the update role fragment
import ma.ensa.volley.beans.Role; // Ensure to use the appropriate Role class
import ma.ensa.volley.R; // Check for the correct R file path

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {
    private List<Role> roleList;
    private Context context;
    private String updateUrl = "http://192.168.126.39:8083/api/v1/roles/%d"; // Update the URL for role update

    public RoleAdapter(List<Role> roleList, Context context) {
        this.roleList = roleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        Role role = roleList.get(position);
        holder.idTextView.setText(String.valueOf(role.getId()));
        holder.nameTextView.setText(role.getName());

        holder.updateButton.setOnClickListener(v -> {
            long roleId = role.getId(); // Get the ID of the selected role
            Fragment updateRoleFragment = new UpdateRoleFragment(); // Ensure to specify the correct name for the update role fragment
            Bundle args = new Bundle();
            args.putLong("roleId", roleId); // Pass the role ID to the update fragment
            updateRoleFragment.setArguments(args);
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, updateRoleFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Do you want to delete this role?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int itemPosition = holder.getAdapterPosition(); // Get the position of the clicked item
                            if (itemPosition != RecyclerView.NO_POSITION) {
                                deleteRole(roleList.get(itemPosition).getId(), itemPosition);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    static class RoleViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView;
        Button updateButton, deleteButton;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView); // Ensure the ID matches your item_role.xml
            nameTextView = itemView.findViewById(R.id.nameTextView); // Ensure the ID matches your item_role.xml
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void deleteRole(long id, int position) {
        String deleteUrl = String.format(updateUrl, id);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        roleList.remove(position); // Remove the item from the list at the current position
                        notifyItemRemoved(position); // Notify the adapter that an item has been removed at the specified position
                        notifyItemRangeChanged(position, roleList.size()); // Notify the adapter that the item range has changed
                        Toast.makeText(context, "Role deleted", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}