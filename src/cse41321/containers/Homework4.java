package cse41321.containers;

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

            private SinglyLinkedList getOwner() {
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
     * @param number1
     * @param number2
     */
    static void addLargeNumbers(String number1, String number2) {
        Stack<Character> firstOperand = new Stack<>();
        Stack<Character> secondOperand = new Stack<>();
        Stack<Character> sum = new Stack<>();
        // Remove any commas or fractional components from the first argument.
        String noPunctuation = number1.replaceAll("[.].*$|[^0-9]", "");
        // Push the remaining characters from the first argument onto a stack.
        for (int index = 0; index < noPunctuation.length(); index++) {
            firstOperand.push(noPunctuation.charAt(index));
        }
        // Remove any commas or fractional components from the second argument.
        noPunctuation = number2.replaceAll("[.].*$|[^0-9]", "");
        // Push the remaining characters from the second argument onto a stack.
        for (int index = 0; index < noPunctuation.length(); index++) {
            secondOperand.push(noPunctuation.charAt(index));
        }

        int intermediateResult; // This variable will act as the accumulator.
        String digits;          // This object will store a string representation of the sum (e.g., "67").
        Stack<Integer> carry = new Stack<>();   //
        // While there are digits remaining in either operand (i.e., character stack),
        // continue looping.
        while (!firstOperand.isEmpty() || !secondOperand.isEmpty()) {
            intermediateResult = 0;
            if (!firstOperand.isEmpty()) {
                intermediateResult += Integer.parseInt(firstOperand.pop().toString());
            }
            if (!secondOperand.isEmpty()) {
                intermediateResult += Integer.parseInt(secondOperand.pop().toString());
            }
            intermediateResult += carry.isEmpty() ? 0 : carry.pop();
            digits = String.valueOf(intermediateResult);
            sum.push(digits.charAt(digits.length() - 1));
            // Did the addition operation result in a carry?
            if (digits.length() > 1) {  // If the number of digits in the answer exceeds 1…
                carry.push(Integer.parseInt(String.valueOf(digits.charAt(0))));
            }
        }
        for (Character digit : sum) System.out.print(digit);
        System.out.println();
    }

}

class DriverClass {

    public static void main(String[] args) {
        String aLargeNumber = "8,129,498,165,026,350,236.5678";
        String aLargerNumber = "18,274,364,583,929,273,748,525.1234";
        Homework4.addLargeNumbers("592.25", "3,784.50");
        Homework4.addLargeNumbers(aLargeNumber, aLargerNumber);
        Homework4.addLargeNumbers("100.101", "400.201");
    }

}