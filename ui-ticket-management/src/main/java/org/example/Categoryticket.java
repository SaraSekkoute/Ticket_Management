package org.example;

public class Categoryticket {
    private Long categoryid;

    private String categoryName;

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Categoryticket{" +
                "categoryid=" + categoryid +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
