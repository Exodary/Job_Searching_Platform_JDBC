package model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Category {
    private Long id;
    private String name;
    private Administrator author;

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name, Administrator author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Administrator getAuthor() {
        return author;
    }

    public void setAuthor(Administrator author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id != null ? id.equals(category.id) : category.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
