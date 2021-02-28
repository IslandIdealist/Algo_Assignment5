import java.util.LinkedList;
import java.util.ArrayList;


/**
 * CISC 380 Algorithms Assignment 5
 *
 * Represents a graph of nodes and edges in adjacency list format.
 *
 * @author Jesse Smrekar & Matt Jerrard
 * Due Date: 04/27/20
 */

public class TwoWayDirectedGraph {

    private ArrayList<TwoWayDirectedGraphNode> nodes;

    public TwoWayDirectedGraph(boolean[][] adjacencyMatrixUphill, boolean[][] adjacencyMatrixDownhill) {
        this.nodes = new ArrayList<TwoWayDirectedGraphNode>();

        // populate the graph with nodes.
        for (int i = 0; i < adjacencyMatrixUphill.length; i++) {
            this.nodes.add(new TwoWayDirectedGraphNode(i));
        }

        // connect the nodes based on the adjacency matrix
        for (int i = 0; i < adjacencyMatrixUphill.length; i++) {
            for (int j = 0; j < adjacencyMatrixUphill[i].length; j++) {
                if (adjacencyMatrixUphill[i][j]) {
                    this.connect(i, j, true);
                }
            }
        }

        // connect the nodes based on the adjacency matrix
        for (int i = 0; i < adjacencyMatrixDownhill.length; i++) {
            for (int j = 0; j < adjacencyMatrixDownhill[i].length; j++) {
                if (adjacencyMatrixDownhill[i][j]) {
                    this.connect(i, j, false);
                }
            }
        }
    }

    public int getGraphSize() {
        return this.nodes.size();
    }// getGraphSize

    private void connect(int root, int other, boolean isUphill) {

        if (0 > root || root >= this.getGraphSize()) {
            throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + root
                    + ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");
        }

        if (0 > other || other >= this.getGraphSize()) {
            throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + other
                    + ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");

        }

        TwoWayDirectedGraphNode rootNode = findNode(root);
        TwoWayDirectedGraphNode otherNode = findNode(other);

        if (isUphill) {
            rootNode.addUphillNodes(otherNode);
        }
        else {
            rootNode.addDownhillNodes(otherNode);
        }


    }// connect

    private TwoWayDirectedGraphNode findNode(int data) {
        if(0 <= data && data < this.nodes.size()){
            return nodes.get(data);
        }else{
            return null;
        }


    }// findNode

    public ArrayList<TwoWayDirectedGraphNode> getNodes() {
        return this.nodes;
    }

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
            TwoWayDirectedGraphNode current = this.nodes.get(i);
            sb.append(String.format("Node: %-8s Uphill Edges: %-3d Downhill Edges: %-3d Uphill Nodes: %-3s Downhill Nodes: %-3s\n", current.data, current.getOutgoingNodesUphill().size(), current.getOutgoingNodesDownhill().size(), this.getArrayData(current.getOutgoingNodesUphill()), this.getArrayData(current.getOutgoingNodesDownhill())));
        }
        return sb.toString();
    }// toString

    private String getArrayData(LinkedList<TwoWayDirectedGraphNode> output) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < output.size(); i++) {
            sb.append(output.get(i).data + ", ");
        }
        sb.append("]");
        return sb.toString();
    }








    /**
     * This method evaluates the nodes and their edges to see if there is an uphill and downhill path from home to work
     * @param homeNode - the home node (starting point)
     * @param workNode - the work node (ending point)
     * @return will return true if there is such a path, and false if there is no such path
     */
    public boolean isValidUphillDownhillPath(int homeNode, int workNode) {

        return isValidUphillDownhillPath( findNode(homeNode), findNode(workNode), true);
    }



    /**
     * Private recursive method for isValidUpgillDownhillPath (public)
     * @param current - the node traversed to so far (starting point)
     * @param workNode - the work node (ending point)
     * @return will return true if there is such a path, and false if there is no such path
     */
    private boolean isValidUphillDownhillPath(TwoWayDirectedGraphNode current, TwoWayDirectedGraphNode workNode, boolean goingUp) {


        /** BASE CASES */
        if(current == workNode){
            return true;
        }

        boolean result = false;

        if(goingUp){

            for(TwoWayDirectedGraphNode child : current.getOutgoingNodesUphill()){

                result = result || isValidUphillDownhillPath( child, workNode, true );
            }
        }

        for(TwoWayDirectedGraphNode child : current.getOutgoingNodesDownhill()){

            result = result || isValidUphillDownhillPath( child, workNode, false );
        }

        return result;
    }











    /**
     * This method will print a pretty version of a valid up/down path if one exists.
     * In this format:
     *
     *     start -> X -> Y -> Z -> end
     *
     * This is not a part of the assignment but it helps for troubleshooting.
     *
     * @param homeNode the starting node
     * @param workNode the target node
     */


    public void printPath(int homeNode, int workNode) {

        ArrayList<TwoWayDirectedGraphNode> array = new ArrayList<TwoWayDirectedGraphNode>();

        if( isValidUphillDownhillPath( findNode(homeNode), findNode(workNode), true) ) {

            createPrintable(findNode(homeNode), findNode(workNode), true, array);

            for (int i = array.size() - 2; i > 0; i--) {

                System.out.print(array.get(i).data + " -> ");
            }
            System.out.println(array.get(0).data);
        }

        else{

            System.out.println("No valid path found.");
        }
    }


    /**
     * private method used by the printPath method
     *
     * @param current node that has been traversed to
     * @param workNode the target node
     * @param goingUp boolean determines if you have started going down yet
     * @param array array of nodes taken
     * @return a boolean for continuity with the actual method
     */
    private boolean createPrintable(TwoWayDirectedGraphNode current, TwoWayDirectedGraphNode workNode, boolean goingUp, ArrayList<TwoWayDirectedGraphNode> array) {


        /** BASE CASES */
        if(current == workNode){
            array.add(current);
            return true;
        }

        boolean result = false;

        if(goingUp){

            for(TwoWayDirectedGraphNode child : current.getOutgoingNodesUphill()){

                result = result || createPrintable( child, workNode, true, array );
                if(result){
                    array.add(current);
                    break;
                }
            }
        }

        for(TwoWayDirectedGraphNode child : current.getOutgoingNodesDownhill()){

            result = result || createPrintable( child, workNode, false, array );
            if(result){
                array.add(current);
                break;
            }
        }

        return result;
    }











    /**
     * This class represents each specific node in the graph.  Each node can have a LinkedList of uphill and downhill nodes to make
     * it a two-way directed graph node.  
     */
    private static class TwoWayDirectedGraphNode {

        private int data;

        private LinkedList<TwoWayDirectedGraphNode> outgoingNodesUphill;
        private LinkedList<TwoWayDirectedGraphNode> outgoingNodesDownhill;

        public TwoWayDirectedGraphNode(int data) {

            this.data = data;
            this.outgoingNodesUphill = new LinkedList<TwoWayDirectedGraphNode>();
            this.outgoingNodesDownhill = new LinkedList<TwoWayDirectedGraphNode>();

        }

        public void addUphillNodes(TwoWayDirectedGraphNode newNode) {
            this.outgoingNodesUphill.add(newNode);
        }

        public void addDownhillNodes(TwoWayDirectedGraphNode newNode) {
            this.outgoingNodesDownhill.add(newNode);
        }

        public LinkedList<TwoWayDirectedGraphNode> getOutgoingNodesUphill() {
            return this.outgoingNodesUphill;
        }

        public LinkedList<TwoWayDirectedGraphNode> getOutgoingNodesDownhill() {
            return this.outgoingNodesDownhill;
        }

    }

}