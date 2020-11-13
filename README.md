# prog6 - TreapMap

Submission for the CS314H TreapMap assignment by Krish Singal.

## Organization

 * `src/` - contains my implementation of the TreapMap
 	* `balanceKarma.java` - *KARMA* contains analysis of balance factor for differing treap sizes
 	* `TreapMap.java` - implements treap operations, TreapNode, and TreapMapIterator
 * `test/` - contains various JUnit testing classes for the implementations in my solution
 	* `AdditionalTreapMapTest.java` - contains JUnit tests for treap operations implemented in `TreapMap.java` using String and Character
	* `BogusTreap.java` - contains implementation of another Treap that is used for join() testing in `TreapMapTest.java` and                                              `AdditionalTreapMapTest.java`
	* `ExtraKey.java` - contains implementation of custom key used for testing in `TreapMapTest.java` and `AdditionalTreapMapTest.java`
	* `PeerTreapMapTest.java` - contains JUnit tests for peer reviews on treap operations implemented in `TreapMap.java` 
   	* `TreapMapTest.java` - contains JUnit tests for treap operations implemented in `TreapMap.java` using Integer, ExtraKey, and String
	* `TreapNodeTest.java` - contains JUnit tests for my implementation of TreapNode in `TreapMap.java`


