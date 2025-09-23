package pt.ua.tqs;


import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Stack<T>{
    LinkedList<T> list = new LinkedList<>();

    public void push(T t){
        list.add(0,t);
    }

    public T pop(){
        if (list.isEmpty()) throw new NoSuchElementException();
        return list.pop();
    }

    public T popN(int n){
        if (n < 1) throw new IllegalArgumentException();
        if (list.isEmpty()) throw new NoSuchElementException();

        for (int i = 1; i < n; i++) {
            if (list.isEmpty()) throw new NoSuchElementException();
            list.pop();
        }
        return list.pop();
    }

    public T peek(){
        if (list.isEmpty()) throw new NoSuchElementException();
        return list.peek();
    }

    public int size(){
        return list.size();
    }
    public boolean isEmpty(){
        return list.isEmpty();
    }

}
