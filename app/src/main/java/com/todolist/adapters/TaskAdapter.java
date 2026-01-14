package com.todolist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.github.zagum.switchicon.SwitchIconView;
import com.todolist.R;
import com.todolist.models.Task;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskComplete(Task task, boolean isComplete);
        void onTaskDelete(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateList(List<Task> newList) {
        taskList.clear();
        taskList.addAll(newList);
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cardTask;
        SwitchIconView switchCompleted;
        TextView tvTaskTitle, tvTaskDescription, tvTaskDate, tvTaskPriority;
        View viewPriorityIndicator;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardTask = itemView.findViewById(R.id.cardTask);
            switchCompleted = itemView.findViewById(R.id.switchCompleted);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            tvTaskDate = itemView.findViewById(R.id.tvTaskDate);
            tvTaskPriority = itemView.findViewById(R.id.tvTaskPriority);
            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
            
            // Animation au clic sur la carte
            cardTask.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(taskList.get(position));
                }
            });
            
            // Switch pour compléter la tâche
            switchCompleted.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = taskList.get(position);
                    boolean isChecked = switchCompleted.isIconEnabled();
                    switchCompleted.switchState();
                    listener.onTaskComplete(task, !isChecked);
                }
            });
            
            // Menu plus (suppression)
            itemView.findViewById(R.id.btnMore).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTaskDelete(taskList.get(position));
                }
            });
        }

        void bind(Task task) {
            // Titre et description
            tvTaskTitle.setText(task.getTitle());
            tvTaskDescription.setText(task.getDescription());
            
            // État complet
            switchCompleted.setIconEnabled(task.isCompleted());
            if (task.isCompleted()) {
                tvTaskTitle.setAlpha(0.5f);
                tvTaskDescription.setAlpha(0.5f);
                cardTask.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.bg_gray));
            } else {
                tvTaskTitle.setAlpha(1f);
                tvTaskDescription.setAlpha(1f);
                cardTask.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.bg_light));
            }
            
            // Date
            if (task.getDueDate() != null) {
                String dateStr = dateFormat.format(task.getDueDate());
                if (task.isToday()) {
                    tvTaskDate.setText("Aujourd'hui");
                    tvTaskDate.setTextColor(
                        itemView.getContext().getColor(R.color.accent));
                } else {
                    tvTaskDate.setText(dateStr);
                    tvTaskDate.setTextColor(
                        itemView.getContext().getColor(R.color.text_secondary));
                }
            } else {
                tvTaskDate.setText("Pas de date");
            }
            
            // Priorité
            switch (task.getPriority()) {
                case HIGH:
                    tvTaskPriority.setText("Haute");
                    tvTaskPriority.setTextColor(
                        itemView.getContext().getColor(R.color.priority_high));
                    viewPriorityIndicator.setBackgroundColor(
                        itemView.getContext().getColor(R.color.priority_high));
                    break;
                case MEDIUM:
                    tvTaskPriority.setText("Moyenne");
                    tvTaskPriority.setTextColor(
                        itemView.getContext().getColor(R.color.priority_medium));
                    viewPriorityIndicator.setBackgroundColor(
                        itemView.getContext().getColor(R.color.priority_medium));
                    break;
                case LOW:
                    tvTaskPriority.setText("Basse");
                    tvTaskPriority.setTextColor(
                        itemView.getContext().getColor(R.color.priority_low));
                    viewPriorityIndicator.setBackgroundColor(
                        itemView.getContext().getColor(R.color.priority_low));
                    break;
            }
            
            // Animation
            itemView.setAlpha(0f);
            itemView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(getAdapterPosition() * 50)
                    .start();
        }
    }
}
