/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.util;

import java.util.Arrays;

/**
 *
 * @author user
 */
public class IntArray 
{
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private int[] array;
    private int size;

    public IntArray()
    {
        array = new int[10];
        size = 0;
    }
    
    public IntArray(int... array)
    {
        this();
        for(int i : array)
            add(i);
    }

    public void clear()
    {
        array = new int[10];
        size = 0;
    }
    
    public final void add(int i)
    {
        ensureCapacity(size + 1);
        array[size++] = i;
        
    }
    
    public final void add(int... value)
    {
        for(int i : value)
            add(i);
    }
    
    public final void insert(int index, int value)
    {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);  
        System.arraycopy(array, index, array, index + 1,
                         size - index);
        array[index] = value;
        size++;
    }
    
    public final void insert(int index, int... value)
    {
        for(int i = 0; i<value.length; i++)
            insert(index+i, value[i]);
    }
    
    public final int remove(int index)
    {
        rangeCheck(index);
        int oldValue = array[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(array, index+1, array, index,
                             numMoved);
        size--;
        return oldValue;
    }

    public final int[] remove(int fromIndex, int toIndex)
    {        
        int removeSize = toIndex - fromIndex;
        int[] arr = new int[removeSize];
        
        for(int i = 0; i<removeSize; i++)
            arr[i] = remove(fromIndex);
        
        return arr;
    }
    
    public final void set(int index, int value)
    {
        array[index] = value;
    }

    public final int get(int index)
    {
        return array[index];
    }

    public final int size()
    {
        return size;
    }

    public final int[] trim()
    {
        if(size < array.length)
            array = Arrays.copyOf(array, size);        
        return array;
    }
    
    public final int[] subArray(int fromIndex, int toIndex)
    {
        subListRangeCheck(fromIndex, toIndex, size);        
        return Arrays.copyOfRange(array, fromIndex, toIndex);        
    }
    
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("index out of bound " +index);
    }
    
    private void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
    
    private void ensureCapacity(int minCapacity)
    {
        if(minCapacity - array.length > 0)
            grow(minCapacity);
    }
    
    private void grow(int minCapacity)
    {
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        array = Arrays.copyOf(array, newCapacity);
    }
    
    private int hugeCapacity(int minCapacity) 
    {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    @Override
    public String toString()
    {
        return Arrays.toString(trim());
    }
}
