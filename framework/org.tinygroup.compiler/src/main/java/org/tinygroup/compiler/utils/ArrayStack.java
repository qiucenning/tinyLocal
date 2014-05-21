package org.tinygroup.compiler.utils;


/**
 *
 * @author Zqq
 */
public final class ArrayStack<T>  {

    private Object[] elements;
    private int size;
    public final static int initialCapacity = 16;

    public ArrayStack() {
        this(initialCapacity);
    }

    public ArrayStack(int initialCapacity) {
        elements = new Object[initialCapacity];
        size = 0;
    }

    public boolean empty() {
        return size == 0;
    }

    public void clear() {
        int i = this.size;
        final Object[] myElements = this.elements;
        while (i != 0) {
            --i;
            myElements[i] = null;
        }
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void pops(int len) {
        int i;
        if ((i = this.size) >= len) {
            final Object[] myElements = this.elements;
            this.size = i - len;
            while (len != 0) {
                myElements[--i] = null;
                len--;
            }
        } else {
            throw new IndexOutOfBoundsException("size < "+len);
        }
    }

    public void push(final T element) {
        final int i;
        Object[] _elements;
        if ((i = size++) >= (_elements = elements).length) {
            System.arraycopy(_elements, 0,
                    _elements = elements = new Object[i << 1], 0, i);
        }
        _elements[i] = element;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        int i;
        if ((i = --size) >= 0) {
            final T element = (T) elements[i];
            elements[i] = null;
            return element;
        } else {
            size = 0;
            throw new IndexOutOfBoundsException("index="+i);
        }
    }

    @SuppressWarnings("unchecked")
    public T peek(int offset) {
        final int realIndex;
        if (offset >= 0 && (realIndex = size - offset - 1) >= 0) {
            return (T) elements[realIndex];
        } else {
            throw new IndexOutOfBoundsException("offset="+ offset);
        }
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        return (T) elements[size - 1];
    }
}