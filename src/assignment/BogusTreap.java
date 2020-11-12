package assignment;

import java.util.Iterator;

public class BogusTreap<K extends Comparable<K>, V> implements Treap<K, V> {
    public V lookup(K key){
        return null;
    }

    public void insert(K key, V value){

    }

    public V remove(K key){
        return null;
    }

    public Treap<K, V> [] split(K key){
        return null;
    }

    public void join(Treap<K, V> t){

    }

    public void meld(Treap<K, V> t) throws UnsupportedOperationException{

    }

    public void difference(Treap<K, V> t) throws UnsupportedOperationException{

    }

    public String toString(){
        return "";
    }

    public Iterator<K> iterator(){
        return null;
    }

    public double balanceFactor() throws UnsupportedOperationException{
        return 0.0;
    }
}
