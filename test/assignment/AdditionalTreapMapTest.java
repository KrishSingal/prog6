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

public class AdditionalTreapMapTest {

    Map<String, Character> strings;
    Map<String, Character> stringsJoin;
    ArrayList<TreapNode> inorder = new ArrayList<>();

    boolean heap = true;
    boolean dupl= true;
    boolean nu = true;
    boolean tos = true;
    
    @Before
    public void setUp(){
        boolean done = false;
        int amt = (int) (Math.random()*50);
        
        done = false;
        strings = new HashMap<>();
        for(int i=0; i< amt; i++){
            do{
                int length = (int)(Math.random()*5) + 1;
                String now ="";
                for (int j=0; j< length; j++){
                    now += (char) ((int) (Math.random() * 26) + 97);
                }
                
                char rand = (char) ((int) (Math.random() * 26) + 97);

                if(strings.get(now) == null){
                    strings.put(now, rand);
                    done = true;
                }

            }while(!done);
            done = false;
        }
        

        // Initializing the join maps

        done = false;
        stringsJoin = new HashMap<>();
        for(int i=0; i< amt; i++){
            do{
                int length = (int)(Math.random()*5) + 1;
                String now ="";
                for (int j=0; j< length; j++){
                    now += (char) ((int) (Math.random() * 3) + 125);
                }

                char rand = (char) ((int) (Math.random() * 26) + 97);

                if(stringsJoin.get(now) == null){
                    stringsJoin.put(now, rand);
                    done = true;
                }

            }while(!done);
            done = false;
        }

    }

    public String buildString (TreapNode root, int tab_amt){
        String ret = "";

        if(root != null) {

            for (int i = 0; i < tab_amt; i++) {
                ret += "\t";
            }
            ret += root;

            ret += buildString(root.left, tab_amt +1);
            ret += buildString(root.right, tab_amt +1);

        }

        return ret;
    }

    @Test
    public void stringstringIRTest(){

        Treap<String, Character> tmap = new TreapMap<>();
        Map<String, Character> contained = new HashMap<>();
        Map<String, Character> notContained = new HashMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet()) {
            notContained.put(entry.getKey(), entry.getValue());
        }

        Iterator<String> ti = tmap.iterator();

        // Insertion

        for (Map.Entry<String, Character> entry : strings.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());

            contained.put(entry.getKey(), entry.getValue());
            notContained.remove(entry.getKey());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkProperties(((TreapMap<String, Character>) tmap).root));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<String, Character> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<String, Character> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        // Look-up

        for (Map.Entry<String, Character> entry : strings.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());

        System.out.println("strings in stringstringIRTest");
        for (Map.Entry<String, Character> entry : strings.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        // Removal

        for (Map.Entry<String, Character> entry : strings.entrySet()) {
            assertEquals(tmap.remove(entry.getKey()), entry.getValue());

            contained.remove(entry.getKey());
            notContained.put(entry.getKey(), entry.getValue());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkProperties(((TreapMap<String, Character>) tmap).root));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<String, Character> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<String, Character> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        // Check that the entire treap is null now
        for (Map.Entry<String, Character> entry : strings.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), null);

    }

    @Test
    public void stringstringSplitJoinBelowRangeTest(){
        // Split
        // Testing a negative split amount

        System.out.println("strings in stringstringSplitJoinTest");
        for (Map.Entry<String, Character> entry : strings.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        Treap<String, Character> splitter = new TreapMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        System.out.println("Splitter treap:" + splitter);

        String splitKey = new String("@$%#");
        Treap<String, Character> [] subtreaps = splitter.split(splitKey);
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[0]).root));
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[1]).root));
        assertTrue(checkLessstrings(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterstrings(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<String, Character> entry : strings.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitKey);

        /*
        Joining them back together
         */

        Treap<String, Character> firstLow = subtreaps[0];
        Treap<String, Character> secondLow = subtreaps[1];

        firstLow.join(secondLow);
        assertTrue(checkProperties(((TreapMap<String, Character>) firstLow).root));

        for (Map.Entry<String, Character> entry : strings.entrySet()){
            assertEquals(firstLow.lookup(entry.getKey()), entry.getValue());
        }

    }


    @Test
    public void stringsStringSplitJoinDotTest(){
        // Testing a split amount on the dot in the key range

        Treap<String, Character> splitter = new TreapMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        System.out.println("Splitter treap:" + splitter);
        //List<String> keys = (ArrayList<String>) strings.keySet();

        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Character> entry : strings.entrySet())
            keys.add(entry.getKey());

        System.out.println("ArrayList of all the keys:" + keys);

        String splitKey = keys.get((int)(Math.random()*keys.size()));
        Treap<String, Character> subtreaps[] = splitter.split(splitKey);
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[0]).root));
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[1]).root));
        assertTrue(checkLessstrings(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterstrings(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<String, Character> entry : strings.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt for on the dot: " + splitKey);

        /*
        Joining them back together
         */

        Treap<String, Character> firstDot = subtreaps[0];
        Treap<String, Character> secondDot = subtreaps[1];

        firstDot.join(secondDot);
        assertTrue(checkProperties(((TreapMap<String, Character>) firstDot).root));

        for (Map.Entry<String, Character> entry : strings.entrySet()){
            assertEquals(firstDot.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void stringstringSplitJoinInRangeTest(){
        // Testing a split amount in the key range but not on the dot

        Treap<String, Character> splitter = new TreapMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Character> entry : strings.entrySet())
            keys.add(entry.getKey());

        String splitKey = "";
        boolean finished = false;
        do{
            int length = (int)(Math.random()*5) + 1;
            String now ="";
            for (int j=0; j< length; j++){
                now += (char) ((int) (Math.random() * 26) + 97);
            }

            if(strings.get(now) == null){
                splitKey = now;
                finished = true;
            }
        } while(!finished);


        Treap<String, Character> subtreaps [] = splitter.split(splitKey);
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[0]).root));
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[1]).root));
        assertTrue(checkLessstrings(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterstrings(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<String, Character> entry : strings.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitKey);

        /*
        Joining them back together
         */

        Treap<String, Character> firstMid = subtreaps[0];
        Treap<String, Character> secondMid = subtreaps[1];

        firstMid.join(secondMid);
        assertTrue(checkProperties(((TreapMap<String, Character>) firstMid).root));

        for (Map.Entry<String, Character> entry : strings.entrySet()){
            assertEquals(firstMid.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void stringstringSplitJoinAboveRangeTest() {
        // Testing a split amount above the key range

        Treap<String, Character> splitter = new TreapMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        String splitKey = "}$*%";
        Treap<String, Character> subtreaps []= splitter.split(splitKey);
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[0]).root));
        assertTrue(checkProperties(((TreapMap<String, Character>) subtreaps[1]).root));
        assertTrue(checkLessstrings(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterstrings(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<String, Character> entry : strings.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitKey);

        /*
        Joining them back together
         */

        Treap<String, Character> firstHigh = subtreaps[0];
        Treap<String, Character> secondHigh = subtreaps[1];

        firstHigh.join(secondHigh);
        assertTrue(checkProperties(((TreapMap<String, Character>) firstHigh).root));

        for (Map.Entry<String, Character> entry : strings.entrySet()){
            assertEquals(firstHigh.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void stringsJoinTest(){
        Treap<String, Character> tmap = new TreapMap<>();
        Treap<String, Character> tmapJoin = new TreapMap<>();

        for (Map.Entry<String, Character> entry : strings.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Character> entry : stringsJoin.entrySet()) {
            tmapJoin.insert(entry.getKey(), entry.getValue());
        }

        tmap.join(tmapJoin);
        assertTrue(checkProperties(((TreapMap<String, Character>) tmap).root));
        System.out.println(tmap);

        // Check that everything from strings and stringsJoin is in the joined treap
        for (Map.Entry<String, Character> entry : strings.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<String, Character> entry : stringsJoin.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }
    }

    /*public boolean checkBSTstrings(TreapNode root){
        inorder.clear();
        inOrder(root, inorder);
        String last = "";
        boolean ret = true;

        System.out.println(inorder);

        for(int i=0; i< inorder.size(); i++){
            String now = (String) inorder.get(i).key;
            if(i !=0 && now.compareTo(last) <0){
                ret = false;

            }
            last = now;
        }
        return ret;
    }*/

    public boolean checkProperties(TreapNode root){
        inorder.clear();

        heap = true;
        dupl= true;
        nu = true;
        tos = true;

        inOrder(root, inorder, new HashSet<String>());
        String last = "";

        for(int i=0; i< inorder.size(); i++){
            String now = (String) inorder.get(i).key;
            if(i !=0 && now.compareTo(last) <0){
                return false;

            }
            last = now;
        }
        Treap<String, String> stringTester = new TreapMap<>(root);
        tos = stringTester.toString() .equals(buildString(root,0));
        return (heap && dupl && nu && tos);
    }

    public void inOrder(TreapNode root, ArrayList<TreapNode> inorder, HashSet<String> seen){
        if(root!= null){
            inOrder(root.left, inorder, seen);

            inorder.add(root);
            if(root.key == null || root.value == null){
                nu = false;
            }
            if((root.left!= null && root.left.priority> root.priority) || (root.right!= null && root.right.priority> root.priority)){
                heap = false;
            }
            if(seen.contains(root.key)){
                dupl = false;
            }
            seen.add((String)root.key);

            inOrder(root.right, inorder, seen);
        }
    }

    public boolean checkLessstrings(Iterator<String> ti, String splitKey){
        while(ti.hasNext()){
            String now = ti.next();
            if(now.compareTo(splitKey)>=0){
                return false;
            }
        }
        return true;
    }

    public boolean checkGreaterstrings(Iterator<String> ti, String splitKey){
        while(ti.hasNext()){
            String now = ti.next();
            if(now.compareTo(splitKey)<0){
                return false;
            }
        }
        return true;
    }

    /*
    @Test
    public void EdgeCasestringsTest(){
        Treap<String, Character> tmap = new TreapMap<>();
        Iterator ti = tmap.iterator();

        // Tests on empty treap

        assertNull(tmap.remove(new String(0.0))); // removing arbitrary value on empty treap should return null

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.remove(null);
        }); // removing null should throw exception

        tmap.split(new String(0.0)); // splitting should be allowed but should do nothing

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.split(null);
        }); // splitting across null should throw an exception

        // Duplicate insertion

        tmap.insert(new String(5.0), "krish");
        tmap.insert(new String(5.0), "singal");

        assertEquals(tmap.lookup(new String(5.0)), "singal");

        assertNull(tmap.lookup(null));

        assertNull(tmap.lookup(new String(100000.0)));
        assertNull(tmap.remove(new String(100000.0)));



        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, "Krish");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(new String(100000.0), null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, null);
        });

        Treap<String, Character> bogus = new BogusTreap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.join(bogus);
        });

    }*/
}
