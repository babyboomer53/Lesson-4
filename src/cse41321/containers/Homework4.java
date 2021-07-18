package cse41321.containers;

import java.util.NoSuchElementException;

import static org.testng.Assert.assertTrue;

public class Homework4 {

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

    static class Stack<E> {
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
    }

    static void loadNumber(String source, Stack<Character> destination) {

    }

    public static class InvalidNumericCharacter extends Exception {
        public InvalidNumericCharacter(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * @param number1
     * @param number2
     */
    static void addLargeNumbers(String number1, String number2) {
        Stack<Character> firstAddend = new Stack<>();
        Stack<Character> secondAddend = new Stack<>();
        Stack<Character> sum = new Stack<>();
        // Remove any commas or fractional component from the first number.
        String noPunctuation = number1.replaceAll("[,_]|[.].*$", "");
        // Push the characters comprising the number onto a stack. This will effectively
        // reverse  the order  of the numbers, so  that the lowest ordered digit will be
        // pushed  last. The  digits will be retrievedfrom the stack in a last-in-first-
        // out order, restoring their original order so that the number can be processed
        // from right to left.
        for (int index = 0; index < noPunctuation.length(); index++) {
            firstAddend.push(noPunctuation.charAt(index));
        }
        // Remove any commas or fractional component from the second number.
        noPunctuation = number2.replaceAll("[,_]|[.].*$", "");
        // This  process is identical to the one applied to the first argument (refer to
        // comment above) except that the digits contained in this string will be pushed
        // onto a separate stack.
        for (int index = 0; index < noPunctuation.length(); index++) {
            secondAddend.push(noPunctuation.charAt(index));
        }

        int intermediateResult = 0;
        String digits;
        Stack<Integer> carry = new Stack<>();

        while (!firstAddend.isEmpty() || !secondAddend.isEmpty()) {
            if (firstAddend.isEmpty()) intermediateResult = 0;
            else intermediateResult = Integer.parseInt(firstAddend.pop().toString());
            if (secondAddend.isEmpty()) intermediateResult += 0;
            else intermediateResult += Integer.parseInt(secondAddend.pop().toString());
            intermediateResult += carry.isEmpty() ? 0 : carry.pop();
            digits = String.valueOf(intermediateResult);
            sum.push(digits.charAt(digits.length() - 1));
            if (digits.length() == 2) {
                carry.push(Integer.parseInt(String.valueOf(digits.charAt(0))));
            }
        }

        while (!sum.isEmpty()) System.out.print(sum.pop());
        System.out.println();
    }

    public static void main(String[] args) {
        Homework4.addLargeNumbers("592.25", "3,784.50");
        Homework4.addLargeNumbers("18,274,364,583,929,273,748,525.1234", "");
    }
}