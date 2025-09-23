package pt.ua.tqs;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    @Test
    void testIsEmptyOnCreation() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testSizeOnCreation() {
        Stack<Integer> stack = new Stack<>();
        assertEquals(0, stack.size());
    }

    @Test
    void testPushAndSize() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        assertEquals(2, stack.size());
        assertFalse(stack.isEmpty());
    }

    @Test
    void testPopReturnsLastPushed() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.pop());
        assertEquals(1, stack.size());
    }

    @Test
    void testPeekReturnsLastWithoutRemoving() {
        Stack<String> stack = new Stack<>();
        stack.push("X");
        stack.push("Y");
        assertEquals("Y", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void testPopOnEmptyThrows() {
        Stack<Double> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
    }


    @Test
    void testPopNThrows() {
        Stack<String> stack = new Stack<>();

        stack.push("X");
        stack.push("Y");
        stack.push("D");
        stack.push("C");
        stack.push("B");
        stack.push("A");

        assertEquals("X", stack.popN(6));
    }

    @Test
    void testPeekOnEmptyThrows() {
        Stack<Double> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    @Test
    void testEmptyAfterPoppingAll() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }
}
