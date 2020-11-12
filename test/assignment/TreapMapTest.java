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

public class TreapMapTest {
    Map<Integer, Integer> ints;
    Map<ExtraKey, String> extras;
    Map<Integer, Integer> intsJoin;
    Map<ExtraKey, String> extrasJoin;

    @Before
    public void setUp(){
        int amt = (int) (Math.random()*50);
        boolean done = false;

        ints = new HashMap<>();
        for(int i =0; i< amt; i++){
            do {
                int rik = (int) (Math.random() * 10000);
                int riv = (int) (Math.random() * 10000);

                if(ints.get(rik) == null ){
                    ints.put(rik, riv);
                    done =true;
                }
            } while(!done);
            done = false;
        }
        ints.put(9999, 0);

        done = false;
        extras = new HashMap<>();
        for(int i=0; i< amt; i++){
            do{
                double rand = Math.random()*1000;
                int length = (int)(Math.random()*5) + 1;
                String now ="";
                for (int j=0; j< length; j++){
                    now += (char) ((int) (Math.random() * 26) + 97);
                }

                ExtraKey toPut = new ExtraKey(rand);

                if(extras.get(toPut) == null){
                    extras.put(toPut, now);
                    done = true;
                }

            }while(!done);
            done = false;
        }
        extras.put(new ExtraKey(999.9), "kr");

        // Initializing the join maps
        done = false;
        intsJoin = new HashMap<>();
        for(int i =0; i< amt; i++){
            do {
                int rikj = (int) (Math.random() * 10000) + 10000;
                int rivj = (int) (Math.random() * 10000) + 10000;

                if(intsJoin.get(rikj) == null ){
                    intsJoin.put(rikj, rivj);
                    done =true;
                }
            } while(!done);
            done = false;
        }
        intsJoin.put(10000, 1);

        done = false;
        extrasJoin = new HashMap<>();
        for(int i=0; i< amt; i++){
            do{
                double rand = Math.random()*1000 + 1000;
                int length = (int)(Math.random()*5);
                String now ="";
                for (int j=0; j< length; j++){
                    now += (char) ((int) (Math.random() * 128));
                }

                ExtraKey toPut = new ExtraKey(rand);

                if(extrasJoin.get(toPut) == null){
                    extrasJoin.put(toPut, now);
                    done = true;
                }

            }while(!done);
            done = false;
        }
        extrasJoin.put(new ExtraKey(1000.0), "k");

    }

    @Test
    public void IntIntIRTest(){

        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();

        Map<Integer, Integer> contained = new HashMap<>();
        Map<Integer, Integer> notContained = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            notContained.put(entry.getKey(), entry.getValue());
        }

        Iterator<Integer> ti = tmap.iterator();

        // Insertion

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());

            contained.put(entry.getKey(), entry.getValue());
            notContained.remove(entry.getKey());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkBSTints(ti));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<Integer, Integer> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<Integer, Integer> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        System.out.println(tmap);

        // Look-up

        for (Map.Entry<Integer, Integer> entry : ints.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());

        System.out.println(ints);
        System.out.println(intsJoin);

        // Removal

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            assertEquals(tmap.remove(entry.getKey()), entry.getValue());

            contained.remove(entry.getKey());
            notContained.put(entry.getKey(), entry.getValue());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkBSTints(ti));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<Integer, Integer> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<Integer, Integer> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        // Check that the entire treap is null now
        for (Map.Entry<Integer, Integer> entry : ints.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), null);

    }

    @Test
    public void IntIntSplitJoinBelowRangeTest(){
        // Split
        // Testing a negative split amount
        Treap<Integer, Integer> splitter = new TreapMap<>();

        for (Map.Entry<Integer, Integer> entry : ints.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        int splitAmt = (int)(Math.random()*10000) - 10000;
        Treap<Integer, Integer> [] subtreaps = splitter.split(splitAmt);
        assertTrue(checkBSTints(subtreaps[0].iterator()));
        assertTrue(checkBSTints(subtreaps[1].iterator()));
        assertTrue(checkLessints(subtreaps[0].iterator(), splitAmt));
        assertTrue(checkGreaterints(subtreaps[1].iterator(), splitAmt));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<Integer, Integer> entry : ints.entrySet()){
            if(entry.getKey()< splitAmt){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitAmt);

        /*
        Joining them back together
         */

        Treap<Integer, Integer> firstLow = subtreaps[0];
        Treap<Integer, Integer> secondLow = subtreaps[1];

        firstLow.join(secondLow);
        assertTrue(checkBSTints(firstLow.iterator()));

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            assertEquals(firstLow.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void IntIntSplitJoinInRangeTest(){
        // Testing a split amount within the key range

        Treap<Integer, Integer> splitter = new TreapMap<>();

        for (Map.Entry<Integer, Integer> entry : ints.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        int splitAmt = (int)(Math.random()*10000);
        Treap<Integer, Integer> subtreaps [] = splitter.split(splitAmt);
        assertTrue(checkBSTints(subtreaps[0].iterator()));
        assertTrue(checkBSTints(subtreaps[1].iterator()));
        assertTrue(checkLessints(subtreaps[0].iterator(), splitAmt));
        assertTrue(checkGreaterints(subtreaps[1].iterator(), splitAmt));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<Integer, Integer> entry : ints.entrySet()){
            if(entry.getKey()< splitAmt){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitAmt);

        /*
        Joining them back together
         */

        Treap<Integer, Integer> firstMid = subtreaps[0];
        Treap<Integer, Integer> secondMid = subtreaps[1];

        firstMid.join(secondMid);
        assertTrue(checkBSTints(firstMid.iterator()));

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            assertEquals(firstMid.lookup(entry.getKey()), entry.getValue());
        }

    }

    @Test
    public void IntIntSplitJoinAboveRangeTest(){
        // Testing a split amount above the key range

        Treap<Integer, Integer> splitter = new TreapMap<>();

        for (Map.Entry<Integer, Integer> entry : ints.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        int splitAmt = (int)(Math.random()*10000) + 10000;
        Treap<Integer, Integer> subtreaps [] = splitter.split(splitAmt);
        assertTrue(checkBSTints(subtreaps[0].iterator()));
        assertTrue(checkBSTints(subtreaps[1].iterator()));
        assertTrue(checkLessints(subtreaps[0].iterator(), splitAmt));
        assertTrue(checkGreaterints(subtreaps[1].iterator(), splitAmt));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<Integer, Integer> entry : ints.entrySet()){
            if(entry.getKey()< splitAmt){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitAmt);

        /*
        Joining them back together
         */

        Treap<Integer, Integer> firstHigh = subtreaps[0];
        Treap<Integer, Integer> secondHigh = subtreaps[1];

        firstHigh.join(secondHigh);
        assertTrue(checkBSTints(firstHigh.iterator()));

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            assertEquals(firstHigh.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void IntIntJoinTest(){
        Treap<Integer, Integer> tmap = new TreapMap<>();
        Treap<Integer, Integer> tmapJoin = new TreapMap<>();

        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : intsJoin.entrySet()) {
            tmapJoin.insert(entry.getKey(), entry.getValue());
        }

        tmap.join(tmapJoin);
        assertTrue(checkBSTints(tmap.iterator()));

        // Check that everything from ints and intJoin is in the joined treap
        for (Map.Entry<Integer, Integer> entry : ints.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : intsJoin.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }
    }

    public boolean checkBSTints(Iterator<Integer> ti){
        int index =0;
        boolean ret = true;
        Integer last = null;
        while(ti.hasNext()){
            Integer now = ti.next();
            if(index !=0 && now.compareTo(last) <0){
                ret = false;

            }
            last = now;
        }
        return ret;
    }

    public boolean checkLessints(Iterator<Integer> ti, Integer splitKey){
        while(ti.hasNext()){
            Integer now = ti.next();
            if(now >= splitKey){
                return false;
            }
        }
        return true;
    }

    public boolean checkGreaterints(Iterator<Integer> ti, Integer splitKey){
        while(ti.hasNext()){
            Integer now = ti.next();
            if(now < splitKey){
                return false;
            }
        }
        return true;
    }

    @Test
    public void specialTest(){
        // Should we be able to add different value types?
        Treap tmaps = new TreapMap();
        tmaps.insert("krish", 5);
        tmaps.insert("shak","sams");
    }

    @Test
    public void EdgeCaseTest(){
        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();
        Iterator ti = tmap.iterator();

        // Tests on empty treap

        assertNull(tmap.remove(50)); // removing arbitrary value on empty treap should return null

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.remove(null);
        }); // removing null should throw exception

        tmap.split(50); // splitting should be allowed but should do nothing

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.split(null);
        }); // splitting across null should throw an exception

        // Duplicate insertion

        tmap.insert(5, -1);
        tmap.insert(5, -5);

        assertEquals((int)tmap.lookup(5), -5);

        /*
        lookup on null and keys not in treap
         */

        assertNull(tmap.lookup(null));

        assertNull(tmap.lookup(10001));
        assertNull(tmap.remove(100001));

        /*
        Insertion of null
         */

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, 5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(5, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, null);
        });

        /*
        Join with a treap that is not treapmap
         */

        Treap<Integer, Integer> bogus = new BogusTreap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.join(bogus);
        });
    }

    @Test
    public void IteratorTest(){
        Treap<ExtraKey, String> tmap = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            tmap.insert(entry.getKey(), entry.getValue());

        Iterator<ExtraKey> ti = tmap.iterator();

        while(ti.hasNext()){
            ti.next();
        }
        assertThrows(NoSuchElementException.class, () -> {
            ti.next();
        });

        Iterator<ExtraKey> ti2 = tmap.iterator();
        ti2.next();
        tmap.insert(new ExtraKey(5.0), "krish");
        assertThrows(ConcurrentModificationException.class, () -> {
            ti2.next();
        });
    }

    @Test
    public void ExtraStringIRTest(){

        Treap<ExtraKey, String> tmap = new TreapMap<>();
        Map<ExtraKey, String> contained = new HashMap<>();
        Map<ExtraKey, String> notContained = new HashMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()) {
            notContained.put(entry.getKey(), entry.getValue());
        }

        Iterator<ExtraKey> ti = tmap.iterator();

        // Insertion

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());

            contained.put(entry.getKey(), entry.getValue());
            notContained.remove(entry.getKey());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkBSTextras(ti));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<ExtraKey, String> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<ExtraKey, String> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        // Look-up

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());

        System.out.println("Extras in ExtraStringIRTest");
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        // Removal

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()) {
            assertEquals(tmap.remove(entry.getKey()), entry.getValue());

            contained.remove(entry.getKey());
            notContained.put(entry.getKey(), entry.getValue());

            // Check that BST property satisfied after each insertion
            ti = tmap.iterator();
            assertTrue(checkBSTextras(ti));

            // Check after each insertion that lookup works on everything in there
            for (Map.Entry<ExtraKey, String> inside : contained.entrySet())
                assertEquals(tmap.lookup(inside.getKey()), inside.getValue());

            for (Map.Entry<ExtraKey, String> outside : notContained.entrySet())
                assertEquals(tmap.lookup(outside.getKey()), null);
        }

        // Check that the entire treap is null now
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            assertEquals(tmap.lookup(entry.getKey()), null);

    }

    @Test
    public void ExtraStringSplitJoinBelowRangeTest(){
        // Split
        // Testing a negative split amount

        System.out.println("Extras in ExtraStringSplitJoinTest");
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        Treap<ExtraKey, String> splitter = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        System.out.println("Splitter treap:" + splitter);

        double splitAmt = Math.random()*1000 - 1000;
        ExtraKey splitKey = new ExtraKey(splitAmt);
        Treap<ExtraKey, String> [] subtreaps = splitter.split(splitKey);
        assertTrue(checkBSTextras(subtreaps[0].iterator()));
        assertTrue(checkBSTextras(subtreaps[1].iterator()));
        assertTrue(checkLessExtras(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterExtras(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitAmt);

        /*
        Joining them back together
         */

        Treap<ExtraKey, String> firstLow = subtreaps[0];
        Treap<ExtraKey, String> secondLow = subtreaps[1];

        firstLow.join(secondLow);
        assertTrue(checkBSTextras(firstLow.iterator()));

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            assertEquals(firstLow.lookup(entry.getKey()), entry.getValue());
        }

    }


    @Test
    public void ExtrasStringSplitJoinDotTest(){
        // Testing a split amount on the dot in the key range

        Treap<ExtraKey, String> splitter = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        System.out.println("Splitter treap:" + splitter);
        //List<ExtraKey> keys = (ArrayList<ExtraKey>) extras.keySet();

        ArrayList<ExtraKey> keys = new ArrayList<>();
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            keys.add(entry.getKey());

        System.out.println("ArrayList of all the keys:" + keys);

        ExtraKey splitKey = keys.get((int)(Math.random()*keys.size()));
        Treap<ExtraKey, String> subtreaps[] = splitter.split(splitKey);
        assertTrue(checkBSTextras(subtreaps[0].iterator()));
        assertTrue(checkBSTextras(subtreaps[1].iterator()));
        assertTrue(checkLessExtras(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterExtras(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt for on the dot: " + splitKey.score);

        /*
        Joining them back together
         */

        Treap<ExtraKey, String> firstDot = subtreaps[0];
        Treap<ExtraKey, String> secondDot = subtreaps[1];

        firstDot.join(secondDot);
        assertTrue(checkBSTextras(firstDot.iterator()));

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            assertEquals(firstDot.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void ExtraStringSplitJoinInRangeTest(){
        // Testing a split amount in the key range but not on the dot

        Treap<ExtraKey, String> splitter = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        ArrayList<ExtraKey> keys = new ArrayList<>();
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            keys.add(entry.getKey());

        boolean finished = true;
        double randKey;
        do{
            randKey = Math.random()*1000;
            finished = true;
            for(int i =0; i< keys.size(); i++){
                if(keys.get(i).score == randKey){
                    finished = false;
                }
            }
        } while(!finished);

        ExtraKey splitKey = new ExtraKey(randKey);
        Treap<ExtraKey, String> subtreaps [] = splitter.split(splitKey);
        assertTrue(checkBSTextras(subtreaps[0].iterator()));
        assertTrue(checkBSTextras(subtreaps[1].iterator()));
        assertTrue(checkLessExtras(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterExtras(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitKey.score);

        /*
        Joining them back together
         */

        Treap<ExtraKey, String> firstMid = subtreaps[0];
        Treap<ExtraKey, String> secondMid = subtreaps[1];

        firstMid.join(secondMid);
        assertTrue(checkBSTextras(firstMid.iterator()));

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            assertEquals(firstMid.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void ExtraStringSplitJoinAboveRangeTest() {
        // Testing a split amount above the key range

        Treap<ExtraKey, String> splitter = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet())
            splitter.insert(entry.getKey(), entry.getValue());

        double splitAmt = Math.random()*1000 + 1000;
        ExtraKey splitKey = new ExtraKey(splitAmt);
        Treap<ExtraKey, String> subtreaps []= splitter.split(splitKey);
        assertTrue(checkBSTextras(subtreaps[0].iterator()));
        assertTrue(checkBSTextras(subtreaps[1].iterator()));
        assertTrue(checkLessExtras(subtreaps[0].iterator(), splitKey));
        assertTrue(checkGreaterExtras(subtreaps[1].iterator(), splitKey));

        // Check to ensure every key, value pair that was in original treap is in either of the sub treaps
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            if(entry.getKey().compareTo(splitKey)<0){
                assertEquals(subtreaps[0].lookup(entry.getKey()), entry.getValue());
            }
            else
                assertEquals(subtreaps[1].lookup(entry.getKey()), entry.getValue());
        }

        System.out.println("Split amt: " + splitAmt);

        /*
        Joining them back together
         */

        Treap<ExtraKey, String> firstHigh = subtreaps[0];
        Treap<ExtraKey, String> secondHigh = subtreaps[1];

        firstHigh.join(secondHigh);
        assertTrue(checkBSTextras(firstHigh.iterator()));

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()){
            assertEquals(firstHigh.lookup(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void ExtrasJoinTest(){
        Treap<ExtraKey, String> tmap = new TreapMap<>();
        Treap<ExtraKey, String> tmapJoin = new TreapMap<>();

        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()) {
            tmap.insert(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<ExtraKey, String> entry : extrasJoin.entrySet()) {
            tmapJoin.insert(entry.getKey(), entry.getValue());
        }

        tmap.join(tmapJoin);
        assertTrue(checkBSTextras(tmap.iterator()));

        // Check that everything from extras and extrasJoin is in the joined treap
        for (Map.Entry<ExtraKey, String> entry : extras.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }

        for (Map.Entry<ExtraKey, String> entry : extrasJoin.entrySet()) {
            assertEquals(tmap.lookup(entry.getKey()), entry.getValue());
        }
    }

    public boolean checkBSTextras(Iterator<ExtraKey> ti){
        int index =0;
        boolean ret = true;
        ExtraKey last = null;
        while(ti.hasNext()){
            ExtraKey now = ti.next();
            if(index !=0 && now.compareTo(last) <0){
                ret = false;
            }
            last = now;
        }
        return ret;
    }

    public boolean checkLessExtras(Iterator<ExtraKey> ti, ExtraKey splitKey){
        while(ti.hasNext()){
            ExtraKey now = ti.next();
            if(now.compareTo(splitKey)>=0){
                return false;
            }
        }
        return true;
    }

    public boolean checkGreaterExtras(Iterator<ExtraKey> ti, ExtraKey splitKey){
        while(ti.hasNext()){
            ExtraKey now = ti.next();
            if(now.compareTo(splitKey)<0){
                return false;
            }
        }
        return true;
    }

    @Test
    public void EdgeCaseExtrasTest(){
        Treap<ExtraKey, String> tmap = new TreapMap<>();
        Iterator ti = tmap.iterator();

        // Tests on empty treap

        assertNull(tmap.remove(new ExtraKey(0.0))); // removing arbitrary value on empty treap should return null

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.remove(null);
        }); // removing null should throw exception

        tmap.split(new ExtraKey(0.0)); // splitting should be allowed but should do nothing

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.split(null);
        }); // splitting across null should throw an exception

        // Duplicate insertion

        tmap.insert(new ExtraKey(5.0), "krish");
        tmap.insert(new ExtraKey(5.0), "singal");

        assertEquals(tmap.lookup(new ExtraKey(5.0)), "singal");

        /*
        lookup on null and keys not in treap
         */

        assertNull(tmap.lookup(null));

        assertNull(tmap.lookup(new ExtraKey(100000.0)));
        assertNull(tmap.remove(new ExtraKey(100000.0)));

        /*
        Insertion of null
         */

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, "Krish");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(new ExtraKey(100000.0), null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tmap.insert(null, null);
        });

        /*
        Join with a treap that is not treapmap
         */

        Treap<ExtraKey, String> bogus = new BogusTreap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            tmap.join(bogus);
        });
    }
}