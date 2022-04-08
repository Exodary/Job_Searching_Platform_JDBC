package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Administrator extends User {
    private List<Category> managedCategories = new ArrayList<>();

    public Administrator(String email, String username, String password, List<Category> managedCategories, List<Job> approvedJobs) {
        super(email, username, password, Role.ADMIN);
        this.managedCategories = managedCategories;
    }

    public Administrator(Long id, String email, String username, String password, Role role) {
        super(id, email, username, password, role);
    }

    public List<Category> getManagedCategories() {
        return managedCategories;
    }

    public void setManagedCategories(List<Category> managedCategories) {
        this.managedCategories = managedCategories;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + super.getId() +
                ", email=" + super.getEmail() +
                "} " + super.toString();
    }
}
