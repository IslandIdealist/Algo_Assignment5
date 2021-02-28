import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * CISC 380 Algorithms Assignment 5
 *
 * Represents a graph of nodes and edges in adjacency list format.
 *
 * @author Jesse Smrekar & Matt Jerrard
 * Due Date: 04/27/20
 */

public class DirectedGraph {

    private static final boolean DEBUG = false;
    private ArrayList<DirectedGraphNode> nodes;
    private ArrayList<DirectedGraphNode> vertexCovers;

    /**
     * Constructs a directed graph with the given adjacency matrix. The adjacency
     * matrix is a 2d array of booleans representing the presence of edges in the
     * graph.
     *
     * An edge from vertex i to vertex j exists if adacencyMatrix[i][j] is true.
     * @param adjacencyMatrix a 2d boolean array representing an adjacency matrix.
     */
    public DirectedGraph(boolean[][] adjacencyMatrix) {

        nodes = new ArrayList<DirectedGraphNode>();

        // populate the graph with nodes.
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            nodes.add(new DirectedGraphNode(i));
        }

        // connect the nodes based on the adjacency matrix
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j]) {
                    this.connect(i, j);
                }//if
            }
        }

        this.vertexCovers = new ArrayList<DirectedGraphNode>(0);
        this.getMinVertexCover();


    }// constructor

    /**
     * Constructs a directed graph with the given adjacency list. The graph will
     * have a number vertices equal to the length of the adjacency list. The
     * adajcency list is a 2d array of integers where the array at index i
     * represents the ids of children of vertex i.
     *
     * Values that are negative or greater than length of adjacencyList are ignored.
     *
     * @param adjacencyList a 2d integer array representing an adjacency list of the vertices.
     */


    public DirectedGraph(int[][] adjacencyList) {  //have an uphill adjacency list and a downhill adjacency list
        nodes = new ArrayList<DirectedGraphNode>();

        // populates the graph with nodes.
        for (int i = 0; i < adjacencyList.length; i++) {
            nodes.add(new DirectedGraphNode(i));
        }

        // connect the nodes based on the adjacency list.
        for (int i = 0; i < adjacencyList.length; i++) {
            for (int j = 0; j < adjacencyList[i].length; j++) {

                // if the value in the array is a valid node id, connect them.
                if (-1 < adjacencyList[i][j] && adjacencyList[i][j] < adjacencyList.length) {
                    this.connect(i, adjacencyList[i][j]);
                }
            }

        }

        this.vertexCovers = new ArrayList<DirectedGraphNode>(0);

    }// constructor

    /**
     * Retrieves the size of the minimum vertex cover of the directed graph. If the
     * graph does not contain a cycle, it sums the minimum vertex cover of each
     * component.
     *
     * @return the minimum vertex cover of the directed graph, otherwise -1.
     */
    public int getMinVertexCover() {

        int result = -1;

        if (this.isAcyclic() && this.hasValidDegrees()) {
            result++;
            for (DirectedGraphNode root : this.findRoots()) {

                result += this.vCover(root);

            }

        } else if (DEBUG) {
            System.out.println("Invalid Graph detected!");
        }

        return result;

    }// getMinVertexCover

    /**
     * Finds the smallest vertex cover for the given directed tree. A vertex cover
     * is a subset of the vertices in the graph that includes one endpoint of every
     * edge in the graph.
     *
     * Runs in linear time.
     *
     * @return the minimum number of nodes to completely cover the graph.
     *
     */
    private int vCover(DirectedGraphNode root) {

        //boolean[][] covered = new boolean[(int)Math.sqrt((double)getGraphSize())][(int)Math.sqrt((double)getGraphSize())];
        //ArrayList<DirectedGraphNode> list = new ArrayList<DirectedGraphNode>();

        /*BASE CASES*/
        if( root.getCoverSize() != null ){

            return root.getCoverSize();
        }

        if(root.getCoverSize() == null) {

            root.setCoverSize(0);
        }

        ListIterator<DirectedGraphNode> it = root.getOutgoingNodes().listIterator();
        boolean added = false;

        //for all children
        while(it.hasNext()){

            DirectedGraphNode node = it.next();
            vCover(node);

            if( (!node.getCovered() && !added) || (isLeaf(node) && !added) ){

                //System.out.println("Node: " + root.getData());
                this.vertexCovers.add(root);
                root.setCoverSize(root.getCoverSize() + node.getCoverSize() + 1);
                root.setCovered();
                added = true;
            }

            else{
                //recurse
                root.setCoverSize(root.getCoverSize() + node.getCoverSize());
            }
        }

        return root.getCoverSize();

        //start at the bottom of the graph(tree) DFS
        //don't add leaves
        //if you add a node, add all children and any parents to the "covered" array
        //check if a node is covered, if not, add it.

    }// vCover

    /**
     * Prints the vertices (the data field for each DirectedGraphNode) included in
     * the smallest vertex cover (i.e. the vertices the correspond to the number
     * returned by vCover(). Note that this method should first call the vCover
     * method to ensure that the needed coverSize fields are updated.
     *
     * Runs in linear time.
     *
     */
    public void printCover() {

        int i;

        System.out.println("Verticies in Vertex Cover");
        for(i=this.vertexCovers.size()-1; i > 0; i--){
            System.out.print(this.vertexCovers.get(i).getData() + ", ");
        }
        System.out.println(this.vertexCovers.get(0).getData());

    }// printCover


    /**
     * returns a boolean of whether a given node is a leaf or not
     *
     *
     * @return a boolean if the node is a leaf
     */
    private boolean isLeaf(DirectedGraphNode node){

        return node.outgoingNodes.size() == 0;
    }











    /**
     * Determines if a cycle exists in the directed graph.
     *
     * @return true, if there are no cycles, false otherwise.
     */
    public boolean isAcyclic() {

        /*

        for( int i=0; i< nodes.size(); i++ ){
            Iterator<LinkedList> iterator = nodes[i].iterator();
        }



         */
        return true;
    }// isAcyclic


    /**
     * Retrieves the number of nodes in the Graph.
     *
     * @return the number of nodes in the graph.
     */
    public int getGraphSize() {
        return this.nodes.size();
    }// getGraphSize

    /**
     * Returns a string representation of all the nodes in the graph. The string
     * displays the nodes data, and a list of all of its outgoing Nodes.
     *
     * @return a string representation of the graph.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // for every node
        for (int i = 0; i < this.nodes.size(); i++) {
            // append the string representation to the result.
            DirectedGraphNode current = this.nodes.get(i);
            sb.append(String.format("%-8s vCover: %3d In: %3d Out: %3d\n", current.toString(),current.getCoverSize(),current.getOutDegree(),current.getInDegree()));
        }
        return sb.toString();
    }// toString


    /**
     * adds the node other as a neighbor to root.
     *
     * @param root  the data of the node to receive a neighbor
     * @param other the data of the node to be added
     */

    private void connect(int root, int other) {

        if (0 > root || root >= this.getGraphSize()) {
            throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + root
                    + ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");
        }

        if (0 > other || other >= this.getGraphSize()) {
            throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + other
                    + ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");

        }

        DirectedGraphNode rootNode = findNode(root);
        DirectedGraphNode otherNode = findNode(other);

        // add the other node to the root
        rootNode.getOutgoingNodes().add(otherNode);
        otherNode.incrementInDegree();

    }// connect

    /**
     * Finds a node in the graph, if it exists.
     *
     * @throws ArrayIndedOutOfBoundsException if the node does not exist.
     * @return a DirectedGraphNode with the given data.
     *
     */

    private DirectedGraphNode findNode(int data) {
        if(0 <= data && data < this.nodes.size()){
            return nodes.get(data);
        }else{
            return null;
        }


    }// findNode

    /**
     * returns an array of integers, representing the vertices that are roots in the
     * Directed Graph
     *
     * @return an array of integers that are roots, or an empty list if there is
     *         none.
     */

    private List<DirectedGraphNode> findRoots() {

        ArrayList<DirectedGraphNode> result = new ArrayList<DirectedGraphNode>();

        // a node is a root if it has an indegree of 0
        // add all nodes with an indegree of 0 to the result array.
        for(DirectedGraphNode node: this.nodes){
            if(node.getInDegree() == 0){
                result.add(node);
            }//if
        }//for

        return result;
    }// findRoots

    /**
     * Checks to see if the in-degrees are valid for the vertex cover algorithm.
     *
     * @return true, if all nodes have an in-degree of 0 or 1. False otherwise.
     *
     */

    private boolean hasValidDegrees() {
        boolean result = true;

        for (DirectedGraphNode node : this.nodes) {
            result = result && (node.getInDegree() == 0 || node.getInDegree() == 1);

        } // for

        return result;

    }// hasValidDegrees



    /**
     * Representation of a vertex of the graph, uniquely identified by the data.
     */
    private static class DirectedGraphNode {

        private int data;
        private int inDegree;
        private boolean covered;
        private Integer coverSize;

        private LinkedList<DirectedGraphNode> outgoingNodes;

        public DirectedGraphNode(int data) {

            this.data = data;
            this.coverSize = null;
            this.outgoingNodes = new LinkedList<DirectedGraphNode>();
            this.inDegree = 0;
            this.covered = false;

        }// constructor

        /**
         * increments the in degree.
         *
         */
        public void incrementInDegree() {
            this.inDegree++;
        }


        /**
         * returns this node's added value.
         * This is a marker if the node has been covered already.
         * @return if this node is already covered.
        */
        public boolean getCovered() { return this.covered; }//getAdded

        public void setCovered() { this.covered = true; }



        /**
         * returns this node's in degree.
         * This is the amount of nodes that this node has as a parent.
         * @return the in degree of this node.
         */

        public int getInDegree() {
            return this.inDegree;
        }//getInDegree

        /**
         * returns this node's out degree.
         * This is the amount of children nodes this node has.
         * @return the out degree of this node.
         */

        public int getOutDegree() {
            return this.outgoingNodes.size();
        }

        /**
         * getter method for the data of the node. This should uniquely identify the
         * node.
         *
         * @return the data within this node.
         */
        public int getData() {
            return this.data;
        }// getData

        /**
         * getter method for the cover size of the node.
         *
         * @return the size of the vertex cover stored in this Node, or null if the
         *         cover size hasn't been set yet.
         *
         */
        public Integer getCoverSize() {
            return this.coverSize;
        }// isCovered

        /**
         * setter method for the cover size.
         *
         * @param size the new size for this node.
         */
        public void setCoverSize(Integer size) {
            this.coverSize = size;
        }// setCoverSize

        /**
         * retrieves a reference to a list of this node's outgoingNodes.
         *
         * @return a LinkedList of nodes that are connected to this node.
         *
         */
        public List<DirectedGraphNode> getOutgoingNodes() {
            return this.outgoingNodes;
        }// getNeighbors

        /**
         * returns a string representation of the node. Displays the current data of the
         * node, and a list of the data of all of its outgoingNodes.
         *
         * @return a string representation of the node.
         */
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(this.getData() + ":[");

            for (int i = 0; i < this.outgoingNodes.size(); i++) {
                if (i == this.outgoingNodes.size() - 1) {
                    sb.append(outgoingNodes.get(i).getData());
                } else {
                    sb.append(outgoingNodes.get(i).getData() + ", ");
                } // else

            } // for
            sb.append("]");
            return sb.toString();

        }// toString







    }// class DirectedGraphNode

}// class