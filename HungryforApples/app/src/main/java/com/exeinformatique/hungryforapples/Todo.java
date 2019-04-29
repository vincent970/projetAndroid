package com.exeinformatique.hungryforapples;

import java.util.Date;

public class Todo {

    String title_resto;
    String description_resto;

    public Todo(String title, String description) {
        this.title_resto = title;
        this.description_resto = description;
    }

    public String getTitle() {
        return title_resto;
    }

    public void setTitle_resto(String title) {
        this.title_resto = title;
    }

    public String getDescription() {
        return description_resto;
    }

    public void setDescription_resto(String description) {
        this.description_resto = description;
    }
}
