    private void updateTaskCount() {
        int totalTasks = taskList.size();
        int completedTasks = dbHelper.getCompletedTasksCount();
        
        if (totalTasks > 0) {
            String progress = "Progression : " + completedTasks + "/" + totalTasks + " (" + 
                (completedTasks * 100 / totalTasks) + "%)";
            tvMotivation.setText(progress);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Rafraîchir la liste après ajout
            loadTasks();
            
            // Message de succès
            Snackbar.make(recyclerViewTasks, "Tâche ajoutée avec succès!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
        customizeGreeting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
