package assignment;
import java.util.*;

public class TestingTreap {
    public static void main(String args[]){
        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();
        tmap.insert(10,6);
        tmap.insert(5,9);
        tmap.insert(-2, 14);
        System.out.println(tmap);
        tmap.insert(-2, -5);
        System.out.println(tmap);
        System.out.println(tmap.lookup(-2));
        System.out.println(tmap.remove(10));
        tmap.split(5);

        /*System.out.println(tmap);
        tmap.split(5);
        System.out.println(tmap);*/

        /*
        Treap<Integer, Integer> [] subs = tmap.split(101);
        System.out.println(subs[0]);
        System.out.println(subs[1]);
        System.out.println(((TreapMap<Integer, Integer>)subs[1]).root == null);
        */

        /*
        System.out.println("Join Test");
        Treap<Integer, Integer> tmap2 = new TreapMap<Integer, Integer>();
        tmap2.insert(-1,4);
        tmap2.insert(0, 50);
        tmap2.insert(4, 0);
        tmap.join(tmap2);
        System.out.println(tmap);
        */


        System.out.println("Meld test");
        Treap<Integer, Integer> tmap2 = new TreapMap<Integer, Integer>();
        tmap2.insert(7, 50);
        tmap2.insert(11,4);
        tmap2.insert(8, 99);

        tmap.join(tmap2);
        System.out.println(tmap);
        System.out.println(tmap2);


        tmap.meld(tmap2);
        System.out.println(tmap);

        Iterator ti = tmap.iterator();
        while(ti.hasNext()){
            System.out.println(ti.next());
        }

        System.out.println(tmap.balanceFactor());

        Treap tmaps = new TreapMap();
        //tmaps.insert(6,5);
        //tmaps.insert(50, 5);
        tmaps.insert('c', 4);
        tmaps.insert('k', 5);
        System.out.println(tmaps.remove('k'));
        //tmaps.insert("krish", 5);

        //tmaps = (Treap<String, Integer>) tmaps;
        //tmaps.insert(5,5);
        //tmap.join(tmaps);

        /*
        System.out.println(tmap2);
        System.out.println(tmap2.remove(8));
        System.out.println(tmap2);

        Iterator ti2 = tmap2.iterator();
        ti2.next();
        ti2.next();
        tmap2.remove(19);
        ti2.next();*/
    }
}
