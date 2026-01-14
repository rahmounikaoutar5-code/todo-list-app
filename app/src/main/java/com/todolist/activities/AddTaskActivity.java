package com.todolist.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.todolist.R;
import com.todolist.database.DatabaseHelper;
import com.todolist.models.Task;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private TextView etDate, etTime;
    private RadioGroup rgPriority;
    private Button btnSave, btnCancel;
    private DatabaseHelper dbHelper;
    
    private Calendar selectedDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        
        // Initialisation
        dbHelper = new DatabaseHelper(this);
        
        // Trouver les vues
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        rgPriority = findViewById(R.id.rgPriority);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        
        // Configurer la date par défaut (demain)
        selectedDateTime.add(Calendar.DAY_OF_MONTH, 1);
        updateDateTimeDisplay();
        
        // Écouteurs
        setupListeners();
    }
    
    private void setupListeners() {
        // Sélecteur de date
        etDate.setOnClickListener(v -> showDatePicker());
        
        // Sélecteur d'heure
        etTime.setOnClickListener(v -> showTimePicker());
        
        // Bouton Annuler
        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        });
        
        // Bouton Sauvegarder
        btnSave.setOnClickListener(v -> saveTask());
        
        // Priorité
        findViewById(R.id.btnPriorityLow).setOnClickListener(v -> {
            rgPriority.check(R.id.btnPriorityLow);
            animatePriorityButton(v);
        });
        
        findViewById(R.id.btnPriorityMedium).setOnClickListener(v -> {
            rgPriority.check(R.id.btnPriorityMedium);
            animatePriorityButton(v);
        });
        
        findViewById(R.id.btnPriorityHigh).setOnClickListener(v -> {
            rgPriority.check(R.id.btnPriorityHigh);
            animatePriorityButton(v);
        });
    }
    
    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH));
        
        // Minimum date = aujourd'hui
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }
    
    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true);
        timePicker.show();
    }
    
    private void updateDateTimeDisplay() {
        // Format date: "dd/MM/yyyy"
        String dateStr = String.format("%02d/%02d/%04d",
                selectedDateTime.get(Calendar.DAY_OF_MONTH),
                selectedDateTime.get(Calendar.MONTH) + 1,
                selectedDateTime.get(Calendar.YEAR));
        etDate.setText(dateStr);
        
        // Format heure: "HH:mm"
        String timeStr = String.format("%02d:%02d",
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE));
        etTime.setText(timeStr);
    }
    
    private void animatePriorityButton(View button) {
        button.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start())
                .start();
    }
    
    private void saveTask() {
        // Validation
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("Le titre est requis");
            etTitle.requestFocus();
            return;
        }
        
        // Créer la tâche
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(etDescription.getText().toString().trim());
        task.setDueDate(selectedDateTime.getTime());
        
        // Priorité
        int selectedId = rgPriority.getCheckedRadioButtonId();
        if (selectedId == R.id.btnPriorityLow) {
            task.setPriority(Task.Priority.LOW);
        } else if (selectedId == R.id.btnPriorityMedium) {
            task.setPriority(Task.Priority.MEDIUM);
        } else if (selectedId == R.id.btnPriorityHigh) {
            task.setPriority(Task.Priority.HIGH);
        }
        
        // Sauvegarder dans la base de données
        long taskId = dbHelper.addTask(task);
        
        if (taskId != -1) {
            // Succès
            Snackbar.make(findViewById(android.R.id.content), 
                    "Tâche ajoutée avec succès!", Snackbar.LENGTH_SHORT).show();
            
            // Retourner le résultat
            setResult(RESULT_OK);
            
            // Petite animation avant de fermer
            btnSave.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        btnSave.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> finish())
                                .start();
                    })
                    .start();
            
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        } else {
            // Erreur
            Snackbar.make(findViewById(android.R.id.content), 
                    "Erreur lors de l'ajout", Snackbar.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }
}
