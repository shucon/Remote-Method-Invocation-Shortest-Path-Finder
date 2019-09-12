# Shared Shortest Path Finder
 ## Distributed Systems Assignment 3

### Saksham Gupta
### 20161090

## Running Application

Compile java files and start RMI Registry (In server directory) 
``$ javac *.java``
``$ rmiregistry``

Start Server
``$ java Server <ip_address>``

Start Client (In client directory)
``javac *.java``
``java Client <ip_address>``

## Commands

- ``add_edge <node1> <node2> <graph name>``
Adds Edge to the graph with given graph name. creates graph if not present already
- ``shortest_distance <node1> <node2> <graph name>``
Returns shortest distance between any two nodes on any graph. Gives Error if graph not present
- ``get_graph <graph name>``
Prints all edges of requested graph

### Assumption
Edge Added using add_edge is directional from node1 to node2
