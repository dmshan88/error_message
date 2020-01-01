package com.example.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**通知主体*/
public class NotifySubject<T extends Serializable> {
    
    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
}
