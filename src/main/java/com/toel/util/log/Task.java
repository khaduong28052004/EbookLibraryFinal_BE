package com.toel.util.log;
import java.util.concurrent.Callable;
import java.util.List;

public abstract class Task<E> implements Callable<Integer>  {
    private List<E> items;
    public void setItems(List<E> items){
        this.items = items;
    }
    public List<E> getItems(){
        return items;
    }
}
