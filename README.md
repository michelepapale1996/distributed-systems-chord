# Distributed Systems - Chord protocol
Project work for the "Distributed Systems" course at Politecnico di Milano.
Implementation of [this paper](https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf).

This repository contains the code related to the implementation of the Chord protocol.
Two versions of the protocol are implemented (as the paper says):

- Simple Key Location: each node need only know how to contact its current successor node on the identifier circle. Queries for a given identifier could be passed around the circle via these suc- cessor pointers until they encounter a pair of nodes that straddle the desired identifier; the second in the pair is the node the query maps to.
- Scalable Key Location: Each node n maintains a routing table with up to m entries, called the finger table

The library contains 4 different packages:
- chord: it contains the logic of the protocol
- Test: it contains the test we done in **local** to show the correct execution of the protocol
- middleware: it contains the rmi logic to run the library in a **distributed** environment

## Execution
#### How to use the library: example

    Debugger.setDebug(true);
    Node node0 = new Node();
    Node node2 = new Node();
    
    node0.create(3, true);
    node2.join(node0);

    node0.exitFromRing();
    
### Local
To run the Chord library in local you need to instantiate a Node. Its main methods are:
- Node.create(int numBitsIdentifier, boolean isSimpleLookupAlgorithm)
- Node.join(Node knownNode)
- Node.lookUp(int key)
- Node.storeItem(Item item)
- Node.exitFromRing

To create an Item you can use its constructor Item(String name, int module) where module is the max number of nodes in the ring

### Distributed
In a distributed environment you can use:
//tutte le operazioni
//setId
Questo poichè in questo modo si possono creare più nodi nella stessa macchina


## Testing
We have done 3 different types of tests:
- Load Test
- JUnit Test: we have tested the most important operation done by the Chord protocol (lookUp - findSuccessor - storeItem - create - join - exitFromRing) (
 you can find it in Test/NodeTest. )
- Test to show the temporal Complexity for the lookup: we have tested that the temporal complexity for the lookup is a log(N) where N is the number of nodes in the ring. The image below shows it running X different rings and for each ring are done 1000 lookups. (you can find it in Test/TemporalComplexityLookUpTest)

//image
On the x-axis there is the number of max-nodes cointained in the ring, while on the y-axis there is the average number of nodes contacted during the lookup phase. It follows a logarithmic curve (as shown in the paper)

## Project Team
- [Papale Michele](https://github.com/michelepapale1996)
- [Puce Gabriele](https://github.com/gabpuce)
- [Tassi Andrea](https://github.com/andre19a)