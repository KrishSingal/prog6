package assignment;
import java.util.*;

public class TestingTreap {
    public static void main(String args[]){
        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();
        tmap.insert(10,6);
        tmap.insert(5,9);
        tmap.insert(100, 5);
        tmap.insert(-1, 5);
        tmap.insert(5, 10);
        //tmap.remove(5);
        System.out.println(tmap);

        Iterator<Integer> treap_iterator = tmap.iterator();

        System.out.println(treap_iterator.hasNext());


        while(treap_iterator.hasNext()){
            System.out.println(treap_iterator.next());
        }

        // System.out.println(treap_iterator.next());
    }
}
