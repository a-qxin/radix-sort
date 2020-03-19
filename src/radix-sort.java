/**
 *
 * File: radix-sort.java
 * @author github@a-qxin
 * @since 2019-08-30
 * 
 * Summary:
 *      An Implementation of radix sort in Java using a Doubly-Linked List.
 * 
 *      Values in the list are sorted from least significant digit to
 *      most significant digit.
 *      
 *      The 10 positions in the array of LinkedLists are used for each
 *      base 10 radix. In mathematical numerical systems, the radix/base is
 *      the number of unique digits, including zero, used to represent
 *      numbers in a positional numeral system.
 *
 *      For example, for the decimal system, the radix is ten because
 *      it uses the ten digits from 0 through 9.
 *
 *      The following program reads the Numbers.txt file and sorts
 *      the data using the Radix Sort algorithm.
 */

// TODO: rename ambivalent variables
// TODO: add separate output .txt file
// TODO: add [public] test function; replace debug
// TODO: add separate function read in file

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class radixSort
{
    public static void main(String[] args) throws FileNotFoundException
    {
        ArrayList<DoublyLinkedList<Integer>> dynamicArray =
                new ArrayList<DoublyLinkedList<Integer>>(10);
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();

        //local path; reading in csv/txt data ints by line
        String csvFile = "/Users/brendayau/IdeaProjects/radixSort.java/src/com/company/Numbers.txt";
        Scanner scanner = new Scanner(new File(csvFile));
        while(scanner.hasNext())
        {
            dll.append(scanner.nextInt());
        }
        scanner.close();

        //replacing null spaces in dynamic array with empty linked lists
        for (int i = 0; i < 10; i++)
        {
            DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
            dynamicArray.add(ll);
        }

        //perform the radix sort
        dll = sort(dll, dynamicArray);

        //print sorted csv data
        for (int i = 0; i < dll.getLength(); i++)
        {
            System.out.print(dll.get(i) + " ");
            if (i % 30 == 0) //for one-page printing purposes
                System.out.println();
        }
    }

    /**
     * Finds and returns number of digits in a given value.
     * @param num is the given value.
     * @return int number of digits
     */
    public static int getDigitLength(int num)
    {
        if (num == 0)
            return 1;
        int numDigits = 0;
        while (num != 0)
        {
            numDigits++;
            num/=10;
        }
        return numDigits;
    }

    /**
     *  Takes a list and finds the value with the greatest number of digits
     *  using getDigitLength(). Returns this value's number of digits.
     *  @param a doubly-linked list of integer values.
     *  @return int number of digits of the value with the greatest digit length in the list.
     */
    public static int greatestDigitLength(DoublyLinkedList<Integer> a)
    {
        int m = 0;
        for (int i = 0; i < a.getLength(); i++)
        {
            int c = getDigitLength(a.get(i));
            if(c > m)
                m = c;
        }
        return m;
    }

    /**
     * Performs the radix sort using greatestDigitLength().
     * @param a unsorted doubly-linked list of integers
     * @param b an ArrayList of empty doubly linked lists representing digit place (10's place)
     * @return DoublyLinkedList<Integer> of the final sorted list
     */
    public static DoublyLinkedList<Integer> sort(DoublyLinkedList<Integer> a, ArrayList<DoublyLinkedList<Integer>> b)
    {
        int alength = a.getLength();
        int m = greatestDigitLength(a);
        int pow10 = 1;
        for (int digitIndex = 0; digitIndex < m; digitIndex++)
        {
            //test print
            //System.out.println("\n----- " + digitIndex + " digit index");
            for (int i = 0; i < alength; i++)
            {
                int bucketIndex = Math.abs(a.get(i) / pow10) % 10;
                //test print:
                //System.out.println("bucket index = " + bucketIndex);
                b.get(bucketIndex).append(a.get(i));
            }

            DoublyLinkedList<Integer> c = new DoublyLinkedList<Integer>();

            for (int i = 0; i < 10; i++)
            {
                int bucketSz = b.get(i).getLength();
                for (int j = 0; j < bucketSz; j++)
                    c.append(b.get(i).get(j));
            }

            a = new DoublyLinkedList<Integer>();
            for(int i = 0; i < c.getLength(); i++)
                a.append(c.get(i));

//			//test: print all buckets
//			System.out.println("print all buckets:");
//			for (int i = 0; i < b.size(); i++)
//			{
//				System.out.print(i + " - " + b.get(i) + " - ");
//				b.get(i).print();
//			}

            //increments power of 10 for bucket calculation
            pow10 *= 10;

            //clears all buckets for next digit index (next loop)
            b.clear();
            for (int i = 0; i < 10; i++)
            {
                DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
                b.add(ll);
            }

            //test print of array after current digit loop
//			System.out.println("sorted a: ");
//			for (int i = 0; i < a.getLength(); i++)
//			{
//				System.out.print(a.get(i) + " ");
//			}
        }
        return a;
    }
}

class DoublyLinkedList<T>
{
    protected Node<T> head = null;
    protected Node<T> tail = null;
    private int size = 0;

    public void append(T data)
    {
        Node<T> newNode = new Node<T>(data);
        if (head == null)
            head = tail = newNode;
        else
        {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
    }

    public void prepend(T data)
    {
        Node<T> newNode = new Node<T>(data);
        if (head == null)
            head = tail = newNode;
        else
        {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
        }
        size++;
    }

    public void insertAfter(T data, Node<T> prev)
    {
        Node<T> newNode = new Node<T>(data);
        if (prev == null)
        {
            System.out.println("Cannot add after null");
            return;
        }
        else if (head == null)
            head = tail = newNode;
        else if (prev == tail)
        {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        else
        {
            Node<T> sucNode = prev.next;
            newNode.next = sucNode;
            newNode.previous = prev;
            prev.next = newNode;
            sucNode.previous = newNode;
        }
    }

    public Node<T> remove(Node<T> n)
    {
        Node<T> sucNode = n.next;
        Node<T> predNode = n.previous;

        if (sucNode != null)
            sucNode.previous = predNode;
        if (predNode != null)
            predNode.next = sucNode;
        if(n == head)
            head = sucNode;
        if(n == tail)
            tail = predNode;

        size--;
        return n;
    }

    public Node<T> search(T num)
    {
        Node<T> temp = head;
        while (null != temp.next && temp.data != num)
            temp = temp.next;
        if (temp.data == num)
            return temp;
        return null;
    }

    public T get(int index)
    {
        Node<T> temp = head;
        for(int i = 0; i < index; i++)
        {
            temp = temp.next;
        }
        return temp.data;
    }

    public Node<T> getNode(int index)
    {
        Node<T> temp = head;
        for(int i = 0; i< index; i++)
        {
            temp = temp.next;
        }
        return temp;
    }

    public void print()
    {
        printRecursive(head);
        System.out.println("");
    }

    public void printRecursive(Node<T> n)
    {
        if (n != null)
        {
            System.out.print(n.data + " ");
            printRecursive(n.next);
        }
    }

    public void printReverse()
    {
        printReverseRecursive(tail);
        System.out.println("");
    }
    public void printReverseRecursive(Node<T> n)
    {
        if (n != null)
        {
            System.out.print(n.data + " ");
            printReverseRecursive(n.previous);
        }
    }

    public Boolean isEmpty()
    {
        return head == null;
    }

    public int getLength() 
    { 
        return size; 
    }

class Stack extends DoublyLinkedList<Object>
{
    void push(Object data)
    {
        append(data);
    }

    Node<Object> pop(Object data)
    {
        return remove(tail);
    }

    Node<Object> peek()
    {
        return tail;
    }
}

class Queue extends DoublyLinkedList<Object>
{
    void push(Object data)
    {
        prepend(data);
    }

    Node<Object> pop(Object data)
    {
        return remove(head);
    }

    Node<Object> peek()
    {
        return head;
    }
}

class Node<T> 
{
    T data;
    Node<T> previous, next;

    public Node(T data) {
        this.data = data;
        next = null;
        previous = null;
    }
}