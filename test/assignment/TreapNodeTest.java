package assignment;

import assignment.Treap;
import assignment.TreapMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.*;
import java.util.Collection;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class TreapNodeTest {

    @Test
    public void priorityTest(){
        TreapNode<String, Integer> tester = new TreapNode("Krish", 5);
        assertTrue(tester.priority < Treap.MAX_PRIORITY && tester.priority >=0);

        TreapNode<String, Double> tester2 = new TreapNode("Krish", 5.0);
        assertTrue(tester.priority < Treap.MAX_PRIORITY && tester.priority >=0);

        TreapNode<ExtraKey, Integer> tester3 = new TreapNode(new ExtraKey(5.0), 5);
        assertTrue(tester.priority < Treap.MAX_PRIORITY && tester.priority >=0);
    }

    @Test
    public void keyValueTest(){
        TreapNode<String, Integer> tester = new TreapNode("Krish", 5);
        assertTrue(tester.key .equals("Krish"));
        assertTrue(tester.value .equals(5));

        TreapNode<String, Double> tester2 = new TreapNode("Krish", 5.0);
        assertTrue(tester2.key .equals("Krish"));
        assertTrue(tester2.value .equals(5.0));

        TreapNode<ExtraKey, Integer> tester3 = new TreapNode(new ExtraKey(5.0), 5);
        assertTrue(tester3.key .compareTo(new ExtraKey(5.0)) == 0);
        assertTrue(tester3.value .equals(5));
    }

    @Test
    public void toStringTest(){
        TreapNode<String, Integer> tester = new TreapNode("Krish", 5);
        int priority = tester.priority;
        System.out.println(tester);
        String should = "[" + priority + "] "  + "<" + "Krish," + " 5>\n";
        System.out.println(should);
        assertTrue(tester.toString().equals(should));

        TreapNode<String, Double> tester2 = new TreapNode("Krish", 5.0);
        priority = tester2.priority;
        System.out.println(tester2);
        should = "[" + priority + "] "  + "<" + "Krish," + " 5.0>\n";
        System.out.println(should);
        assertTrue(tester2.toString().equals(should));

        TreapNode<ExtraKey, Integer> tester3 = new TreapNode(new ExtraKey(5.0), 5);
        priority = tester3.priority;
        System.out.println(tester3);
        should = "[" + priority + "] "  + "<" + "5.0," + " 5>\n";
        System.out.println(should);
        assertTrue(tester3.toString().equals(should));
    }

}