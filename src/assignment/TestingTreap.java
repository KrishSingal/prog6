package assignment;

public class TestingTreap {
    public static void main(String args[]){
        Treap<Integer, Integer> tmap = new TreapMap<Integer, Integer>();
        tmap.insert(5,6);
        tmap.insert(10,9);
        System.out.println(tmap);
    }
}
