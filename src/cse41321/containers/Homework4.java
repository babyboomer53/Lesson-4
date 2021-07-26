package cse41321.containers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Homework4 {

    static class SinglyLinkedList<E> {
        // An element in a linked list
        public class Element {
            private E data;
            private Element next;

            // Only allow SinglyLinkedList to construct Elements
            private Element(E data) {
                this.data = data;
                this.next = null;
            }

            public E getData() {
                return data;
            }

            public Element getNext() {
                return next;
            }

            private SinglyLinkedList<E> getOwner() {
                return SinglyLinkedList.this;
            }
        }

        private Element head;
        private Element tail;
        private int size;

        public Element getHead() {
            return head;
        }

        public Element getTail() {
            return tail;
        }

        public int getSize() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public Element insertHead(E data) {
            Element newElement = new Element(data);

            if (isEmpty()) {
                // Insert into empty list
                head = newElement;
                tail = newElement;
            } else {
                // Insert into non-empty list
                newElement.next = head;
                head = newElement;
            }

            ++size;

            return newElement;
        }

        public Element insertTail(E data) {
            Element newElement = new Element(data);

            if (isEmpty()) {
                // Insert into empty list
                head = newElement;
                tail = newElement;
            } else {
                // Insert into non-empty list
                tail.next = newElement;
                tail = newElement;
            }

            ++size;

            return newElement;
        }

        public Element insertAfter(Element element, E data)
                throws IllegalArgumentException {
            // Check pre-conditions
            if (element == null) {
                throw new IllegalArgumentException(
                        "Argument 'element' must not be null");
            }
            if (element.getOwner() != this) {
                throw new IllegalArgumentException(
                        "Argument 'element' does not belong to this list");
            }

            // Insert new element
            Element newElement = new Element(data);
            if (tail == element) {
                // Insert new tail
                element.next = newElement;
                tail = newElement;
            } else {
                // Insert into middle of list
                newElement.next = element.next;
                element.next = newElement;
            }

            ++size;

            return newElement;
        }

        public E removeHead() throws NoSuchElementException {
            // Check pre-conditions
            if (isEmpty()) {
                throw new NoSuchElementException("Cannot remove from empty list");
            }

            // Remove the head
            Element oldHead = head;
            if (size == 1) {
                // Handle removal of the last element
                head = null;
                tail = null;
            } else {
                head = head.next;
            }

            --size;

            return oldHead.data;
        }

        // Note that there is no removeTail.  This cannot be implemented
        // efficiently because it would require O(n) to scan from head until
        // reaching the item _before_ tail.

        public E removeAfter(Element element)
                throws IllegalArgumentException, NoSuchElementException {
            // Check pre-conditions
            if (element == null) {
                throw new IllegalArgumentException(
                        "Argument 'element' must not be null");
            }
            if (element.getOwner() != this) {
                throw new IllegalArgumentException(
                        "Argument 'element' does not belong to this list");
            }
            if (element == tail) {
                throw new IllegalArgumentException(
                        "Argument 'element' must have a non-null next element");
            }

            // Remove element
            Element elementToRemove = element.next;
            if (elementToRemove == tail) {
                // Remove the tail
                element.next = null;
                tail = element;
            } else {
                // Remove from middle of list
                element.next = elementToRemove.next;
            }

            --size;

            return elementToRemove.data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SinglyLinkedList<?> that = (SinglyLinkedList<?>) o;

            if (this.size != that.size) return false;

            // Return whether all elements are the same
            SinglyLinkedList<?>.Element thisElem = this.getHead();
            SinglyLinkedList<?>.Element thatElem = that.getHead();
            while (thisElem != null && thatElem != null) {
                if (!thisElem.getData().equals(thatElem.getData())) {
                    return false;
                }
                thisElem = thisElem.getNext();
                thatElem = thatElem.getNext();
            }

            return true;
        }
    }

    static class Stack<E> implements Iterable<E> {
        private SinglyLinkedList<E> list = new SinglyLinkedList<E>();

        public void push(E data) {
            list.insertHead(data);
        }

        public E pop() throws NoSuchElementException {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }

            return list.removeHead();
        }

        public E peek() throws NoSuchElementException {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }

            return list.getHead().getData();
        }

        public int getSize() {
            return list.getSize();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        private class Stackerator implements Iterator<E> {
            private SinglyLinkedList<E>.Element elem;

            public Stackerator() {
                elem = Stack.this.list.getHead();
            }

            public boolean hasNext() {
                return elem != null;
            }

            public E next() {
                if (hasNext()) {
                    E data = elem.getData();
                    elem = elem.getNext();
                    return data;
                } else {
                    throw new NoSuchElementException();
                }
            }
        }

        public Iterator<E> iterator() {
            return new Stackerator();
        }

    }

    /**
     * This  method accepts  two  string  arguments. Each  argument  is expected to
     * contain  numeric characters.  Invalid  characters will  be  removed prior to
     * processing.  The arguments  will be  added together  and the  result will be
     * displayed on the console.
     * <p>
     * This  method can  accommodate numbers  much  larger than  those supported  by
     * Java's  <i>long</i>  integer type  (approx. 9.23 quintillion). Theoretically,
     * this  method can  accommodate integers  that are  two billion  digits long  –
     * although nothing approaching that magnitude has been tested.
     * <p>
     * Arguments containing empty strings are tolerated and are generally ignored.
     * An argument consisting of an empty string is equivalent to the value zero.
     * When both arguments are empty, no output is generated.
     *
     * @param number1 a String representing an integer value
     * @param number2 a String representing an integer value
     * @return a String containing the sum of the equation
     */
    static String addLargeNumbers(String number1, String number2) {
        Stack<Character> firstOperand = new Stack<>();
        Stack<Character> secondOperand = new Stack<>();
        Stack<Character> theSum = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();
        // Remove any commas or fractional components from the first argument.
        String noPunctuation = number1.replaceAll("[.].*$|[^0-9]", "");
        // Push the remaining characters in the first argument onto a stack.
        for (int index = 0; index < noPunctuation.length(); index++) {
            firstOperand.push(noPunctuation.charAt(index));
        }
        // Remove any commas or fractional components from the second argument.
        noPunctuation = number2.replaceAll("[.].*$|[^0-9]", "");
        // Push the remaining characters in the second argument onto a stack.
        for (int index = 0; index < noPunctuation.length(); index++) {
            secondOperand.push(noPunctuation.charAt(index));
        }
        int intermediateResult;
        int carry = 0;
        // Repeat the following steps until both stacks are empty.
        while (!firstOperand.isEmpty() || !secondOperand.isEmpty()) {
            intermediateResult = carry;
            if (!firstOperand.isEmpty()) {
                intermediateResult += Integer.parseInt(firstOperand.pop().toString());
            }
            if (!secondOperand.isEmpty()) {
                intermediateResult += Integer.parseInt(secondOperand.pop().toString());
            }
            // Convert the integer (int) result to char and push it onto the stack.
            theSum.push(Integer.toString(intermediateResult % 10).charAt(0));
            carry = intermediateResult / 10;    // Save the carry amount.
        }
        if (carry > 0) {    // If there is a carry amount left dangling…
            theSum.push(Integer.toString(carry).charAt(0)); // push it onto the result stack.
        }
        System.out.println();
        theSum.forEach(System.out::print);
        theSum.forEach(stringBuilder::append);
        String reversed = stringBuilder.reverse().toString().replaceAll("([0-9]{3})", "$1,");
        return new StringBuilder().append(reversed).reverse().toString();
    }

}

class DriverClass {

    public static void main(String[] args) {
        String aLargeNumber = "8,129,498,165,026,350,236.5678";
        String aLargerNumber = "18,274,364,583,929,273,748,525.1234";
        String aHumongousNumber = "1,234,556,709,877,654,234,389,809,987,678,098,911,232,335,657";
        String anotherHumongousNumber = "7,789,891,212,333,446,789,653,445,689,656,778,890,032,345,433";
        Homework4.addLargeNumbers("592.25", "3,784.50");
        Homework4.addLargeNumbers(aLargeNumber, aLargerNumber);
        Homework4.addLargeNumbers("100.101", "400.201");
        Homework4.addLargeNumbers("5600", "5700");
        Homework4.addLargeNumbers("8300", "850");
        System.out.printf("%n%nThe next operation adds two numbers, each of which contains %d digits!%n",
                aHumongousNumber.replaceAll("[^0-9]*","").length());
        System.out.println("\n"+Homework4.addLargeNumbers(aHumongousNumber, anotherHumongousNumber));

    }

}