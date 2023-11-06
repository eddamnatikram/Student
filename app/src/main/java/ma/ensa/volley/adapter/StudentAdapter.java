package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private Context context;

    public StudentAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.idTextView.setText(String.valueOf(student.getId()));
        holder.nameTextView.setText("Nom et Prénom: "+student.getName());
        holder.phoneTextView.setText(String.valueOf("phone :"+student.getPhone()));
        holder.emailTextView.setText("Adresse Email :"+student.getEmail());

        holder.updateButton.setOnClickListener(v -> {
            // Logique pour l'action de mise à jour
            Toast.makeText(context, "Update clicked for student: " + student.getName(), Toast.LENGTH_SHORT).show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Logique pour l'action de suppression
            Toast.makeText(context, "Delete clicked for student: " + student.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, phoneTextView, emailTextView;
        Button updateButton, deleteButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.studentIdTextView);
            nameTextView = itemView.findViewById(R.id.studentNameTextView);
            phoneTextView = itemView.findViewById(R.id.studentPhoneTextView);
            emailTextView = itemView.findViewById(R.id.studentEmailTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
