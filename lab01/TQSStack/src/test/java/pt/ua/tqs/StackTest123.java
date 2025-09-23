package pt.ua.tqs;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Disabled;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.*;

class StackTest123 {

    // Don't use System.out.println, use a logger instead
    static final Logger log = org.slf4j.LoggerFactory.getLogger(lookup().lookupClass());

    @org.junit.jupiter.api.Test
    public void emptyOnCreation(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing isEmpty method in {}", stack.getClass().getName());
        assertTrue(stack.isEmpty());
    }

    @org.junit.jupiter.api.Test
    public void zeroOnCreation(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing initial size method in {}", stack.getClass().getName());

        assertEquals(0, stack.size());
    }

    @org.junit.jupiter.api.Test
    public void sizeAdapt(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing size method in {}", stack.getClass().getName());

        stack.push(1);
        stack.push(2);

        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
    }

    @org.junit.jupiter.api.Test
    public void popValue(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing pop method in {}", stack.getClass().getName());

        stack.push(123);

        assertEquals(123, stack.pop());
    }

    @Disabled
    @org.junit.jupiter.api.Test
    public void peekUnchanged(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing peek method in {}", stack.getClass().getName());

        stack.push(123);
        int first = stack.size();

        int a = stack.peek();

        assertEquals(first, stack.size() );
    }

    @org.junit.jupiter.api.Test
    public void emptyOnPop(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing size on pop method in {}", stack.getClass().getName());

        stack.push(123);
        stack.push(321);

        stack.pop();
        stack.pop();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @org.junit.jupiter.api.Test
    public void errorPoppingEmpty(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing empty pop method in {}", stack.getClass().getName());

        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @org.junit.jupiter.api.Test
    public void errorPeekingEmpty(){
        Stack<Integer> stack = new Stack<>();
        log.debug("Testing empty peek method in {}", stack.getClass().getName());

        assertThrows(NoSuchElementException.class, stack::peek);
    }
}
