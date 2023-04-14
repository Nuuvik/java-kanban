package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds; //список айдишников сабтасков


    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(Integer id, String title, String description) {
        super(id, title, description);
        subtaskIds = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }


    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }


}
