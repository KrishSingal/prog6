package assignment;
import java.util.Iterator;
import java.util.*;

public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {

    public TreapNode root;
    public int modCount;

    public <K, V> TreapMap (){
        root = null;
        modCount =0;
    }

    public <K, V> TreapMap(TreapNode root){
        this.root = root;
        modCount =0;
    }

    public V lookup(K key){
        if(key == null){
            return null;
        }

        TreapNode found = find(key, root);

        return found == null? null : (V) found.value;
    }

    private TreapNode find(K key, TreapNode root){
        // The node is not found, return null
        if(root == null){
            return null;
        }

        // Node is found
        if(key.compareTo((K) root.key) == 0){
            return root;
        }

        if(key.compareTo((K)root.key) < 0){
            return find(key, root.left);
        }
        else
            return find(key, root.right);
    }

    public void insert (K key, V value) throws IllegalArgumentException{
        modCount++;

        if(key != null && value != null) {
            // Check to ensure user is entering all
            if(root != null) {
                try {
                    key.compareTo((K) root.key);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Cannot make a treap with differing entry types");
                }
            }

            remove(key);
            root = insertRec(new TreapNode(key, value), root);
        }

        else
            throw new IllegalArgumentException("Please insert a non-null entry!");
    }

    // Overloaded insert that recursively inserts and heapifies
    public TreapNode insertRec (TreapNode toInsert, TreapNode root){
        if(root ==null){
            return toInsert;
        }
        if(toInsert == null){
            return root;
        }

        if(toInsert.key.compareTo((K) root.key) <= 0){
            root.left = insertRec(toInsert, root.left);
        }
        else
            root.right= insertRec(toInsert, root.right);

        // On the recursive call back up, we heapify the treap

        if (root.left != null && root.priority < root.left.priority){
            root = rotateRight(root);
        }
        else if (root.right != null && root.priority < root.right.priority) {
            root = rotateLeft(root);
        }

        return root;
    }

    public TreapNode rotateRight(TreapNode pivot){
        TreapNode newRoot = pivot.left;
        pivot.left = newRoot.right;
        newRoot.right = pivot;

        return newRoot;
    }

    public TreapNode rotateLeft(TreapNode pivot){
        TreapNode newRoot = pivot.right;
        pivot.right = newRoot.left;
        newRoot.left = pivot;

        return newRoot;
    }

    public V remove (K key) throws IllegalArgumentException{
        modCount++;

        if(key == null){
            throw new IllegalArgumentException("Please remove for a non-null key!");
        }

        if(root != null){
            try{
                key.compareTo((K)root.key);
            }
            catch(Exception e){
                throw new IllegalArgumentException("Please remove the proper type");
            }
        }

        TreapNode removed = find(key, root);
        root = removeRec(key,root);


        if(removed != null){
            return (V)removed.value;
        }
        else
            return null;
    }

    public TreapNode removeRec(K key, TreapNode root){
        if(root == null){
            return null;
        }

        if(key != null && key.compareTo((K)root.key) <0){
            root.left = removeRec(key, root.left);
        }
        else if(key != null && key.compareTo((K)root.key) >0){
            root.right = removeRec(key, root.right);
        }

        // We have found the node to remove
        else{
            // Case 1: is a leaf node
            if(root.right == null && root.left == null){
                return null;
            }

            // Case 2: has one child
            else if (root.right == null || root.left == null){
                if(root.right != null){
                    root = root.right;
                }
                else{
                    root = root.left;
                }
            }

            // Case 3: has two children
            else{
                if(root.left.priority < root.right.priority){
                    root = rotateLeft(root);
                    root.left = removeRec(key, root.left);
                }
                else if(root.left.priority > root.right.priority){
                    root = rotateRight(root);
                    root.right = removeRec(key, root.right);
                }
            }
        }

        return root;
    }

    public Treap<K, V> [] split (K key) throws IllegalArgumentException{
        modCount++;
        if(key == null){
            throw new IllegalArgumentException("Please split across a non-null key!");
        }

        if(root != null){
            try{
                key.compareTo((K)root.key);
            }
            catch(Exception e){
                throw new IllegalArgumentException("Please split across the proper type");
            }
        }

        return split(this, key);
    }

    public Treap<K, V> [] split(Treap<K, V> t, K key){
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        TreapNode maximum = new TreapNode(key, null);
        maximum.priority = MAX_PRIORITY;
        t_root = insertRec(maximum, t_root);

        Treap [] subTreaps = new TreapMap[2];
        subTreaps[0] = new TreapMap(t_root.left);
        subTreaps[1] = new TreapMap(t_root.right);

        //t_root = removeRec(key, t_root);

        return subTreaps;
    }

    public void join(Treap<K, V> t){
        modCount++;
        if(t == null){
            throw new IllegalArgumentException("Please join a non-null treap!");
        }

        if(! (t instanceof TreapMap)){
            throw new IllegalArgumentException("Please join a treap of the same implementing class!");
        }


        TreapNode t_root = ((TreapMap<K, V>) t).root;

        if(t_root != null && root != null){
            try{
                t_root.key.compareTo(root.key);
            }
            catch(Exception e){
                throw new IllegalArgumentException("Cannot join treaps of different types");
            }
        }

        root = join(this.root, t_root);
    }

    /**
     * Abstracted join method
     */
    public TreapNode join(TreapNode f_root, TreapNode t_root){

        if(f_root == null){
            return t_root;
        }
        else if(t_root ==null){
            return f_root;
        }

        TreapNode arb = new TreapNode(null, null);
        if(t_root.key.compareTo(f_root.key)< 0){
            arb.left = t_root;
            arb.right = f_root;
        }
        else{
            arb.left = f_root;
            arb.right = t_root;
        }

        return removeRec(null, arb);
    }

    public void meld(Treap<K, V> t){
        modCount++;
        if(t == null){
            throw new IllegalArgumentException("Please meld a non-null treap!");
        }

        root = meld(this, t);
    }

    public TreapNode meld (Treap<K,V> f, Treap<K, V> t){
        TreapNode f_root = ((TreapMap<K, V>) f).root;
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        if(f_root == null){
            //System.out.println("f_root is null");
            return t_root;
        }
        else if(t_root ==null){
            //System.out.println("t_root is null");
            //System.out.println("Returning" + f_root.key);
            return f_root;
        }
            Treap<K, V> [] subt = split(t, (K)f_root.key);

            TreapNode newRoot = join(meld(new TreapMap<K, V> (f_root.left), subt[0]), meld(new TreapMap<K, V> (f_root.right), subt[1]));
            TreapNode toPlace = new TreapNode((K)f_root.key, (V)f_root.value);
            toPlace.priority = f_root.priority;
            return insertRec(toPlace, newRoot);
    }

    public void difference(Treap<K, V> t) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not done yet!");
    }

    public String toString() {
        String ret = buildString(root, 0);

        return ret;
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

    public Iterator<K> iterator(){
        return new TreapMapIterator(this);
    }

    public double balanceFactor() {
        int height = height(root);

        Iterator<K> iter = (TreapMapIterator) this.iterator();

        int count=0;
        while(iter.hasNext()){
            iter.next();
            count++;
        }

        int pwr =0;
        while(Math.pow(2, pwr)-1 <= count){
            pwr++;
        }

        int min_height = pwr;

        return (double) height / (double) min_height;
    }

    public int height(TreapNode root){
        if(root == null){
            return 0;
        }

        return 1 + Math.max(height(root.left), height(root.right));
    }
}

class TreapNode<K extends Comparable<K>, V> {
    public TreapNode right;
    public TreapNode left;
    public TreapNode parent;
    public K key;
    public V value;
    public int priority;

    public TreapNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.priority = (int) (Math.random()*65535);
    }

    public String toString(){
        return "[" + priority + "]" + " <" + key + ", " + value + ">" + "\n";
    }
}

class TreapMapIterator<K extends Comparable<K>> implements Iterator<K> {
    TreapNode root;
    Stack<TreapNode> st = new Stack<>();
    int initialModCount;
    TreapMap treapinst;

    public TreapMapIterator (TreapMap inst){
        this.root = inst.root;
        addLeft(root);
        this.initialModCount = inst.modCount;
        this.treapinst = inst;
    }

    public void addLeft(TreapNode curr){
        while(curr!= null){
            st.push(curr);
            curr = curr.left;
        }
    }

    public K next() throws ConcurrentModificationException{
        if(!hasNext()){
            throw new NoSuchElementException("There is no next Element!");
        }

        if(treapinst.modCount != initialModCount){
            throw new ConcurrentModificationException("Can't Modify the treap while iterating!");
        }

        TreapNode ret = st.pop();
        addLeft(ret.right);
        return (K)ret.key;
    }

    public boolean hasNext() throws ConcurrentModificationException{
        if(treapinst.modCount != initialModCount){
            throw new ConcurrentModificationException("Can't Modify the treap while iterating!");
        }
        return !st.isEmpty();
    }
}
