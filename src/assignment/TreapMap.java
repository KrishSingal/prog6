package assignment;
import java.util.Iterator;
import java.util.*;

/**
 * Class that implements TreapMap logic
 * @param <K> Key type
 * @param <V> Value type
 */
public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {

    public TreapNode root;
    public int modCount;

    /**
     * Default constructor
     * @param <K> Key type
     * @param <V> Value type
     */
    public <K, V> TreapMap (){
        root = null;
        modCount =0;
    }

    /**
     * Overloaded constructor that instantiates TreapMap with specified root
     * @param root specified root
     * @param <K> Key type
     * @param <V> Value type
     */
    public <K, V> TreapMap(TreapNode root){
        this.root = root;
        modCount =0;
    }

    /**
     * Look up on certain key
     * @param key   the key whose associated value
     *              should be retrieved
     * @return      value of key to be retrieved
     */
    public V lookup(K key){
        if(key == null){
            return null;
        }

        TreapNode found = find(key, root);

        return found == null? null : (V) found.value;
    }

    /**
     * Helper method to retrieve the TreapNode with specified key
     * @param key   specified key to retrieve
     * @param root  root of Treap
     * @return      TreapNode with specified key
     */
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

    /**
     * Function to perform insertion of specified Key/Value pair
     * @param key      the key to add to this dictionary
     * @param value    the value to associate with the key
     * @throws IllegalArgumentException
     */
    public void insert (K key, V value) throws IllegalArgumentException{
        modCount++;

        if(key != null && value != null) {
            // Check to ensure user is entering valid types
            if(root != null) {
                try {
                    key.compareTo((K) root.key);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Cannot make a treap with differing entry types");
                }
            }

            // If input is correct, remove any pre-existing entry with same key, then
            // add in new entry
            remove(key);
            root = insertRec(new TreapNode(key, value), root);
        }

        else
            throw new IllegalArgumentException("Please insert a non-null entry!");
    }

    /**
     * Overloaded insert that recursively inserts and heapifies
     * @param toInsert      TreapNode to insert in
     * @param root          Root of Treap
     * @return              New root of the Treap
     */
    public TreapNode insertRec (TreapNode toInsert, TreapNode root){
        // Base cases
        if(root ==null){
            return toInsert;
        }
        if(toInsert == null){
            return root;
        }

        // recursive call down, we find the leaf position to insert at
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

    /**
     * Rotates subtreap right around specified pivot
     * @param pivot     Specified pivot
     * @return          New root
     */
    public TreapNode rotateRight(TreapNode pivot){
        TreapNode newRoot = pivot.left;
        pivot.left = newRoot.right;
        newRoot.right = pivot;

        return newRoot;
    }

    /**
     * Rotates subtreap left around specified pivot
     * @param pivot     Specified pivot
     * @return          New root
     */
    public TreapNode rotateLeft(TreapNode pivot){
        TreapNode newRoot = pivot.right;
        pivot.right = newRoot.left;
        newRoot.left = pivot;

        return newRoot;
    }

    /**
     * Function to remove entry with specified key
     * @param key      the key to remove
     * @return         value of removed entry
     * @throws IllegalArgumentException
     */
    public V remove (K key) throws IllegalArgumentException{
        modCount++;

        // invalid input handling
        if(key == null){
            throw new IllegalArgumentException("Please remove for a non-null key!");
        }

        // Check to ensure user is entering proper types
        if(root != null){
            try{
                key.compareTo((K)root.key);
            }
            catch(Exception e){
                throw new IllegalArgumentException("Please remove the proper type");
            }
        }

        // if proper types have been entered, retrieve value of entry to be removed
        // and remove the entry
        TreapNode removed = find(key, root);
        root = removeRec(key,root);


        if(removed != null){
            return (V)removed.value;
        }
        else
            return null;
    }

    /**
     * recursive removal helper function
     * @param key       specified key to remove
     * @param root      root of Treap
     * @return          New root
     */
    public TreapNode removeRec(K key, TreapNode root){
        // Base Case
        if(root == null){
            return null;
        }

        // recursive down to find the node to be removed
        if(key != null && key.compareTo((K)root.key) <0){
            root.left = removeRec(key, root.left);
        }
        else if(key != null && key.compareTo((K)root.key) >0){
            root.right = removeRec(key, root.right);
        }

        // We have found the node to remove
        else{
            // Case 1: is a leaf node
            // snip off node
            if(root.right == null && root.left == null){
                return null;
            }

            // Case 2: has one child
            // replace node to be removed with child
            else if (root.right == null || root.left == null){
                if(root.right != null){
                    root = root.right;
                }
                else{
                    root = root.left;
                }
            }

            // Case 3: has two children
            // rotate to make child with higher priority the root of subtreap
            // then remove node to be removed
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

    /**
     * Split treap around specified key
     * @param key    the key to split the treap with
     * @return       Array containing left and right subtreaps
     * @throws IllegalArgumentException
     */
    public Treap<K, V> [] split (K key) throws IllegalArgumentException{
        modCount++;

        // handle invalid input
        if(key == null){
            throw new IllegalArgumentException("Please split across a non-null key!");
        }

        // ensure user is entering correct types
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

    /**
     * Overloaded split function that splits any arbitrary treap around specified key
     * @param t     treap to be split
     * @param key   specified key to split around
     * @return      array of subtreaps
     */
    public Treap<K, V> [] split(Treap<K, V> t, K key){
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        // insert node with maximum priority and same key as that to be split around
        TreapNode maximum = new TreapNode(key, null);
        maximum.priority = MAX_PRIORITY;
        t_root = insertRec(maximum, t_root);

        // extract left and right subtreaps
        Treap [] subTreaps = new TreapMap[2];
        subTreaps[0] = new TreapMap(t_root.left);
        subTreaps[1] = new TreapMap(t_root.right);

        //t_root = removeRec(key, t_root);

        return subTreaps;
    }

    /**
     * Function to join this treap with an arbitrary TreapMap
     * @param t    the treap to join with
     */
    public void join(Treap<K, V> t){
        modCount++;

        // invalid input handling
        if(t == null){
            throw new IllegalArgumentException("Please join a non-null treap!");
        }

        // check that provided treap is of correct implementing class
        if(! (t instanceof TreapMap)){
            throw new IllegalArgumentException("Please join a treap of the same implementing class!");
        }

        TreapNode t_root = ((TreapMap<K, V>) t).root;

        // Check that types provided by user are correct
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
     * Abstracted join() function to join two arbitrary treaps
     * @param f_root        root of first treap
     * @param t_root        root of second treap
     * @return              root of joined treaps
     */
    public TreapNode join(TreapNode f_root, TreapNode t_root){

        // Base cases
        if(f_root == null){
            return t_root;
        }
        else if(t_root ==null){
            return f_root;
        }

        // make arbitrary node the root and satisfy BST property
        TreapNode arb = new TreapNode(null, null);
        if(t_root.key.compareTo(f_root.key)< 0){
            arb.left = t_root;
            arb.right = f_root;
        }
        else{
            arb.left = f_root;
            arb.right = t_root;
        }

        // remove the arbitrary node and the treap will rebalance itself
        return removeRec(null, arb);
    }

    /**
     * function to meld to this treap with another
     * @param t    the treap to meld with.  t may be modified.
     */
    public void meld(Treap<K, V> t){
        modCount++;
        // invalid input handling
        if(t == null){
            throw new IllegalArgumentException("Please meld a non-null treap!");
        }

        root = meld(this, t);
    }

    /**
     * Abstracted meld function to meld two arbitrary treaps
     * @param f     first treap
     * @param t     second treap
     * @return      root of melded treap
     */
    public TreapNode meld (Treap<K,V> f, Treap<K, V> t){
        TreapNode f_root = ((TreapMap<K, V>) f).root;
        TreapNode t_root = ((TreapMap<K, V>) t).root;

        // base cases
        if(f_root == null){
            return t_root;
        }
        else if(t_root ==null){
            return f_root;
        }

        // split second treap across root of first treap
        Treap<K, V> [] subt = split(t, (K)f_root.key);

        // recursively join the result of meld of the left subtreaps to meld of the right subtreaps
        TreapNode newRoot = join(meld(new TreapMap<K, V> (f_root.left), subt[0]), meld(new TreapMap<K, V> (f_root.right), subt[1]));

        // add the initial root of first subtreap back in
        TreapNode toPlace = new TreapNode((K)f_root.key, (V)f_root.value);
        toPlace.priority = f_root.priority;
        return insertRec(toPlace, newRoot);
    }

    public void difference(Treap<K, V> t) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not done yet!");
    }

    /**
     * returns human readable string representation of the treap
     * @return      human readable string representation of the treap
     */
    public String toString() {
        String ret = buildString(root, 0);

        return ret;
    }

    /**
     * recursively builds the string representation of treap via a pre-order traversal
     * @param root          root of treap
     * @param tab_amt       amount to be tabbed over
     * @return              string representation
     */
    public String buildString (TreapNode root, int tab_amt){
        String ret = "";

        if(root != null) {

            // add in string rep of curr node
            for (int i = 0; i < tab_amt; i++) {
                ret += "\t";
            }
            ret += root;

            // tab children over by 1
            ret += buildString(root.left, tab_amt +1);
            ret += buildString(root.right, tab_amt +1);

        }

        return ret;
    }

    /**
     * returns fresh iterator for this treap
     * @return      fresh iterator for this treap
     */
    public Iterator<K> iterator(){
        return new TreapMapIterator(this);
    }

    /**
     * comptes balance factor for this treap
     * @return      balance factor for this treap
     */
    public double balanceFactor() {
        int height = height(root);

        Iterator<K> iter = (TreapMapIterator) this.iterator();

        // counts number of nodes in treap
        int count=0;
        while(iter.hasNext()){
            iter.next();
            count++;
        }

        // compute in height if we had a complete tree
        int pwr =0;
        while(Math.pow(2, pwr)-1 <= count){
            pwr++;
        }

        int min_height = pwr;

        return (double) height / (double) min_height;
    }

    /**
     * computes height of treap
     * @param root  root of treap
     * @return      height of treap
     */
    public int height(TreapNode root){
        // base case
        if(root == null){
            return 0;
        }

        // recurse to find height of subtreaps
        return 1 + Math.max(height(root.left), height(root.right));
    }
}

/**
 * Class that stores and encapsulates data of nodes in treap
 * @param <K>   key type
 * @param <V>   value type
 */
class TreapNode<K extends Comparable<K>, V> {
    public TreapNode right;
    public TreapNode left;
    public K key;
    public V value;
    public int priority;

    /**
     * constructor the initializes children and assigns random priority to new node
     * @param key   specified key data
     * @param value specified value data
     */
    public TreapNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.priority = (int) (Math.random()*Treap.MAX_PRIORITY);
    }

    /**
     * returns string representation of this node
     * @return
     */
    public String toString(){
        return "[" + priority + "]" + " <" + key + ", " + value + ">" + "\n";
    }
}

/**
 * Class that implements logic for iterator of TreapMap
 * @param <K>   key type
 */
class TreapMapIterator<K extends Comparable<K>> implements Iterator<K> {
    TreapNode root;
    Stack<TreapNode> st = new Stack<>(); // stack used for iteration
    int initialModCount; // initial number of modifications when iterator was made
    TreapMap treapinst; // TreapMap instance that is being iterated

    /**
     * constructor that initializes all data fields
     * @param inst    TreapMap instance that is being iterated
     */
    public TreapMapIterator (TreapMap inst){
        this.root = inst.root;
        addLeft(root);
        this.initialModCount = inst.modCount;
        this.treapinst = inst;
    }

    /**
     * pushes entire left ancestry of specified node into stack
     * @param curr  specified node whose left ancestry should be pushed
     */
    public void addLeft(TreapNode curr){
        while(curr!= null){
            st.push(curr);
            curr = curr.left;
        }
    }

    /**
     * retrieves next element in in-order fashion
     * @return next element in in-order fashion
     * @throws ConcurrentModificationException
     */
    public K next() throws ConcurrentModificationException{
        // Check for invalid cases
        if(!hasNext()){
            throw new NoSuchElementException("There is no next Element!");
        }

        if(treapinst.modCount != initialModCount){
            throw new ConcurrentModificationException("Can't Modify the treap while iterating!");
        }

        // if valid, pop off stack and add left ancestry of right subtree
        // return popped item
        TreapNode ret = st.pop();
        addLeft(ret.right);
        return (K)ret.key;
    }

    /**
     * Checks whether there is next element to iterate in treap
     * @return whether there is next element to iterate in treap
     * @throws ConcurrentModificationException
     */
    public boolean hasNext() throws ConcurrentModificationException{
        if(treapinst.modCount != initialModCount){
            throw new ConcurrentModificationException("Can't Modify the treap while iterating!");
        }
        return !st.isEmpty();
    }
}
