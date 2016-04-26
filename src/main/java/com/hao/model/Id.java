package com.hao.model;

import java.io.Serializable;

/**
 * Created by user on 2016/2/24.
 */
public abstract class Id implements Serializable{

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
