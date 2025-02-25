package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {

    private Long id;
    private String title;
    private String description;
    private String priorityLevel;  // Cette propriété correspondra à un enum en backend, mais ici c'est une String
    private String statusTracking; // Correspond aussi à un enum, mais ici une String
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Categoryticket getCategoryticket() {
        return categoryticket;
    }

    public void setCategoryticket(Categoryticket categoryticket) {
        this.categoryticket = categoryticket;
    }

    private Categoryticket categoryticket;
    private Employee employee;// Vous pouvez adapter selon ce que vous envoyez pour la catégorie
    private Long assignedEmployeeId;  // Id de l'employé assigné

    private List<String> comments; // Liste des commentaires associés au ticket

    // Constructeurs
    public Ticket() {
    }

    public Ticket(Long id, String title, String description, String priorityLevel, String statusTracking, LocalDateTime createdAt, LocalDateTime updatedAt, Categoryticket categoryticket, Long assignedEmployeeId, List<String> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.statusTracking = statusTracking;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryticket = categoryticket;
        this.assignedEmployeeId = assignedEmployeeId;
        this.comments = comments;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getStatusTracking() {
        return statusTracking;
    }

    public void setStatusTracking(String statusTracking) {
        this.statusTracking = statusTracking;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }



    public Long getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(Long assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public Employee getEmployee() {  // Méthode pour récupérer l'employé
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priorityLevel='" + priorityLevel + '\'' +
                ", statusTracking='" + statusTracking + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryticket='" + categoryticket + '\'' +
                ", assignedEmployeeId=" + assignedEmployeeId +
                ", comments=" + comments +
                '}';
    }
}
