# Distributed Systems - Chord protocol
Project work for the "Distributed Systems" course at Politecnico di Milano.
Implementation of [this paper](https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf).

This repository contains the code related to the implementation of the Chord protocol.
Two versions of the protocol are implemented (as the paper says):

- Simple Key Location: each node need only know how to contact its current successor node on the identifier circle. Queries for a given identifier could be passed around the circle via these successor pointers until they encounter a pair of nodes that straddle the desired identifier; the second in the pair is the node the query maps to.
- Scalable Key Location: Each node n maintains a routing table with up to m (where m is = log(n)) entries, called the finger table

The library contains 4 different packages:
- chord: it contains the logic of the protocol
- Test: it contains the test we done in **local** to show the correct execution of the protocol
- middleware: it contains the rmi logic to run the library in a **distributed** environment
- Utilities: it contains helpers to test the protocol
## Execution
#### How to use the library: example

    Node node0 = new Node();
    Node node2 = new Node();
    int numBitsIdentifier = 3;
    boolean isSimpleLookup = true;
    node0.setId(0);
    node2.setId(2);
    node0.create(numBitsIdentifier, isSimpleLookup);
    node2.join(node0);
    node0.exitFromRing();
    
### Local
To run the Chord library in local you need:
- to initialize some nodes (through its constructor **Node()**)
- use its main methods: 
    - **Node.create(int numBitsIdentifier, boolean isSimpleLookupAlgorithm)**
    - **Node.join(Node knownNode)**
    - **Node.lookUp(int key)**
    - **Node.storeItem(Item item)**
    - **Node.exitFromRing()**

To create an Item you can use its constructor **Item(String name, int module)** where module is the max number of nodes in the ring

### Distributed
To use the protocol in a distributed environment, check the example that can be found under middleware/Client.
Please, note that in a distributed environment you have to set the id of the node (through **Node.setId(int id)**) before to create or join a new Chord ring.

## Testing
We have done 3 different types of tests:
- Load Test: it creates a ring doing random actions (between join/storeItems/exit) for 20 seconds. After this time, it is printed the info of a node you want to check the correctness.
- JUnit Test: we have tested the most important operations done by the Chord protocol (lookUp - findSuccessor - storeItem - create - join - exitFromRing) (
 you can find it in Test/NodeTest. )
- Test to show the temporal Complexity for the lookup: we have tested the temporal complexity for the lookup (you can find it in Test/TemporalComplexityLookUpTest)
Each test run 10 different rings (starting from a ring containing 2 nodes to a ring containing 1024 nodes) and for each ring are done 1000 lookups.

We have checked that the lookup in case of simpleKeyLocation is a **O(N)** where N is the number of max nodes in the ring.  

![alt text](https://github.com/michelepapale1996/distributed-systems-chord/blob/master/src/Test/img2.PNG)

It follows a **linear** curve as shown in the paper. 

We have checked that the lookup in case of scalableKeyLocation is a **O(log(N))** where N is the number of max nodes in the ring.

![alt text](https://github.com/michelepapale1996/distributed-systems-chord/blob/master/src/Test/img3.PNG)

It follows a **logarithmic** curve as shown in the paper.

## Project Team
- [Papale Michele](https://github.com/michelepapale1996)
- [Puce Gabriele](https://github.com/gabpuce)
- [Tassi Andrea](https://github.com/andre19a)