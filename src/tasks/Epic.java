package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds; //список айдишников сабтасков
    protected Status status;

    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new ArrayList<>();
    }

    public Epic(Integer id, String title, String description) {
        super(id, title, description);
        subtaskIds = new ArrayList<>();
    }

    public void addSubtask(Integer subtaskID) {
        this.subtaskIds.add(subtaskID);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }


}
