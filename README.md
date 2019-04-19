# Distributed Systems - Chord protocol
Project work for the "Distributed Systems" course at Politecnico di Milano done by 
Implementation of [this relative paper](https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf).

This repository contains the code related to the implementation of the Chord protocol.
Two versions of the protocol are implemented (as the paper says):

- Simple Key Location: each node need only know how to contact its current successor node on the identifier circle. Queries for a given identifier could be passed around the circle via these suc- cessor pointers until they encounter a pair of nodes that straddle the desired identifier; the second in the pair is the node the query maps to.
- Scalable Key Location: Each node n maintains a routing table with up to m entries, called the finger table

The library contains 4 different packages:
- chord: it contains the logic of the protocol
- Test: it contains the test we done in **local** to show the correct execution of the protocol
- middleware: it contains the rmi logic to run the library in a **distributed** environment
- Graphic: it contains the logic to create images of the infos of the nodes (finger tables)

##Execution
###Local
To run the Chord library in local you need to istantiate a Node. Its main methods are:
- create
- join
- lookUp
- storeItem
- exitFromRing

####How to use the library: example

    Debugger.setDebug(true);
    Node node0 = new Node();
    Node node2 = new Node();
    
    node0.create(3, true);
    node2.join(node0);

    node0.exitFromRing();
    


###Distributed

##Testing

##Project Team
- [Papale Michele](https://github.com/michelepapale1996)
- [Puce Gabriele](https://github.com/gabpuce)
- [Tassi Andrea](https://github.com/andre19a)