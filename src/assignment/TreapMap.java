package assignment;
import java.util.Iterator;
import java.util.*;

public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {

    public TreapNode root;

    public TreapMap (){
        root = null;
    }

    public TreapMap(TreapNode root){
        this.root = root;
    }

    public V lookup(K key){
        if(key == null){
            return null;
        }

        return (V) find(key, root).value;
    }

    private TreapNode find(K key, TreapNode root){
        // The node is not found, return null
        if(root == null){
            return null;
        }

        // Node is found
        if(key.equals(root.key)){
            return root;
        }

        if(key.compareTo((K)root.key) < 0){
            return find(key, root.left);
        }
        else
            return find(key, root.right);
    }

    /*public TreapNode iterFind(K key, TreapNode root){
        if(root ==null){
            return null;
        }

        parentFind = null;
        type = ChildType.ROOT;

        while(root != null){
            if(key.compareTo((K) root.key) < 0){
                root = root.left;
                parentFind = root;
                type = ChildType.LEFT;

            }
            else if(key.compareTo((K) root.key) > 0){
                root = root.left;
                parentFind = root;
                type = ChildType.RIGHT;
            }
            else {
                return root;
            }

        }
        parentFind = null;
        type = ChildType.NOT_FOUND;
        return null;
    }*/

    /*
    public void insert(K key, V value){
        TreapNode toInsert = new TreapNode(key, value);

        if(root == null){
            root = toInsert;
        }
        else{
            remove(key); // remove any old value associated with key

            TreapNode parent = root;
            TreapNode curr = root;

            while(curr != null){
                parent = curr;

                if(key.compareTo((K) curr.key) < 0){
                    curr = curr.left;
                }
                else
                    curr = curr.right;
            }

            if(key.compareTo((K) parent.key) < 0){
                parent.left = toInsert;
                toInsert.type = TreapNode.ChildType.LEFT;
            }
            else {
                parent.right = toInsert;
                toInsert.type = TreapNode.ChildType.RIGHT;
            }

            heapify(toInsert.parent);

        }
    }*/

    public void insert (K key, V value){
        if(key != null && value != null) {
            remove(key);
            root = insertRec(new TreapNode(key, value), root);
        }
    }

    // Overloaded insert that recursively inserts and heapifies
    public TreapNode insertRec (TreapNode toInsert, TreapNode root){
        if(root ==null){
            return toInsert;
        }

        if(toInsert.key.compareTo((K) root.key) < 0){
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

    /*public void heapify (TreapNode curr){
        while(curr.parent.priority < curr.priority){
            if(curr.type == TreapNode.ChildType.LEFT){
                rotateRight(curr.parent);
            }
            else if (curr.type == TreapNode.ChildType.RIGHT){
                rotateLeft(curr.parent);
            }
        }
    }*/

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

    // Overloaded insert method that recursively inserts
    /*
    public TreapNode insert (K key, V value, TreapNode root){
        if(root==null){
            return new TreapNode(key,value);
        }

        if((root.key).compareTo(key) > 0){
            root.left = insert(key, value, root.left);
        }
        else{
            root.right = insert(key, value, root.right);
        }

        return root;
    }*/

    /*
    public V remove(K key){
        TreapNode toRemove = iterFind(key, root);

        if(toRemove != null){
            remove(toRemove, parentFind, type);
            return (V) toRemove.value;
        }
        else
            return null;
    }

    public void remove (TreapNode curr, TreapNode parent, ChildType type){
        if(curr.left == null && curr.right == null){
            if(type == ChildType.LEFT){
                parent.left = null;
            }
            else
                parent.right = null;
        }

        else if(curr.right == null || (curr.left != null && curr.left.priority >= curr.right.priority)){
            if(type == ChildType.LEFT){
                parent.left = rotateRight(curr);
                remove(curr, parent.left, ChildType.RIGHT);
            }
            else {
                parent.right = rotateRight(curr);
                remove(curr, parent.left, ChildType.RIGHT);
            }
        }

        else{
            if(type == ChildType.LEFT){
                parent.left = rotateLeft(curr);
                remove(curr, parent.left, ChildType.LEFT);
            }
            else {
                parent.right = rotateLeft(curr);
                remove(curr, parent.left, ChildType.LEFT);
            }
        }
    }*/

    public V remove (K key){
        root = removeRec(key,root);
        TreapNode removed = find(key, root);

        if(removed != null){
            return (V)removed.value;
        }
        else
            return null;
    }

    public TreapNode removeRec( K key, TreapNode root){
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

    public Treap<K, V> [] split(K key){
        TreapNode maximum = new TreapNode(key, null);
        maximum.priority = MAX_PRIORITY;
        root = insertRec(maximum, root);

        Treap [] subTreaps = new TreapMap[2];
        subTreaps[0] = new TreapMap(root.left);
        subTreaps[1] = new TreapMap(root.right);

        return subTreaps;
    }

    public void join(Treap<K, V> t) {
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        TreapNode arb = new TreapNode(null, null);
        if(t_root.key.compareTo(root.key)< 0){
            arb.left = t_root;
            arb.right = root;
        }
        else{
            arb.left = root;
            arb.right = t_root;
        }

        root = removeRec(null, arb);
    }

    public TreapNode getRoot(){
        return root;
    }

    public void meld(Treap<K, V> t){

    }


    public TreapNode meldRec (Treap<K, V> t){
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        if(root == null){
            return t_root;
        }
        if(t_root == null){
            return root;
        }

        Treap<K, V> split_t[] = t.split((K)t_root.key);
        Treap<K, V> split_this[] = split((K)t_root.key);

        return join()
    }

    public void difference(Treap<K, V> t) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not done yet!");
    }

    public String toString() {
        /*String ret ="";

        Stack<TreapNode> nodes = new Stack<TreapNode> ();
        nodes.push(root);
        // Iterative pre-order traversal
        while(!nodes.isEmpty()){
            TreapNode now = nodes.pop();
            ret += now;

            if(now.right != null){
                nodes.push(now.right);
            }
            if(now.left != null){
                nodes.push(now.left);
            }
        }*/

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
        return new TreapMapIterator(root);
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

        int min_height = pwr-1;

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
    ArrayList<K> ordered = new ArrayList<K>();
    int index;

    public TreapMapIterator(TreapNode root){
        this.root = root;
        inOrder(root);
        index =0;
    }

    public void inOrder (TreapNode root){
        if(root!= null) {
            inOrder(root.left);
            ordered.add((K) root.key);
            inOrder(root.right);
        }
    }

    public K next() throws NoSuchElementException{
        if(!hasNext()){
            throw new NoSuchElementException("No more elements!");
        }

        K key_ret = ordered.get(index);
        index++;
        return key_ret;
    }

    public boolean hasNext(){
        return index < ordered.size();
    }
}
