package assignment;
import java.util.*;

public class TestingTreap {
    public static void main(String args[]){
        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();
        tmap.insert(10,6);
        tmap.insert(5,9);
        tmap.insert(-2, 14);

        System.out.println(tmap);


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
        tmap2.insert(-1,4);
        tmap2.insert(8, 99);


        System.out.println(tmap);
        System.out.println(tmap2);

        tmap.meld(tmap2);
        System.out.println(tmap);

        Iterator ti = tmap.iterator();
        while(ti.hasNext()){
            System.out.println(ti.next());
        }


    }
}
