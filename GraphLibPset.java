/**
 * @author Chipo Chibbamulilo
 * @author Chikwanda Chisha
 * Graph library implementing BFS, getPath,missingV,averageSeparation and verticesInDegree
 */

import java.util.*;

public class GraphLibPset<V,E> {

    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
        //instantiate graph
        Graph<V,E> pathTree= new AdjacencyMapGraph<>();

        //adding first element to graph
        pathTree.insertVertex(source);

        //instantiate set to keep the visited
        Set<V> visited=new HashSet<>();

        // instantiate queue
        Queue<V> queue=new LinkedList<>();

        queue.add(source);
        visited.add(source);

        while(!queue.isEmpty()){
            V v= queue.remove();

            for (V i: g.outNeighbors(v) ){
                //check if been visited
                if(!visited.contains(i)) {
                    visited.add(i);
                    queue.add(i);

                    //add vertex and directed edge to the graph
                    pathTree.insertVertex(i);
                    pathTree.insertDirected(i, v, g.getLabel(i, v));
                }
            }
        }
        return pathTree;
    }

    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
       // instantiate new list
       ArrayList<V> path= new ArrayList<>();

        path.add(v);
        V u=v;

        //adding vertices from pathtree to path(Arraylist)
        while(tree.outNeighbors(u).iterator().hasNext()){
           path.addAll((Collection<? extends V>) tree.outNeighbors(u));

           //u's changed to the next out neighbor
           u= tree.outNeighbors(u).iterator().next();
        }
       return path;
    }

    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) throws Exception {
        //instantiate set and map
        Set<V> missingV= new HashSet<>();

        //sending the graphs over to a helper function that returns a map
        Map<V, Integer> freqmap= mapFrequencies(subgraph,graph);

        //checking the frequencies of the elements
        for(V v: freqmap.keySet()){

            //only add to set if v has a frequency of one
            if(freqmap.get(v)==1){
                missingV.add(v);
            }
        }

        return missingV;
    }

    public static <V,E> Map<V, Integer> mapFrequencies(Graph<V,E> g1, Graph<V,E> g2) throws Exception{
        if (g1==null|| g2==null) throw new Exception("One or Both of the Trees is null");

        Map<V,Integer> freqmap= new HashMap<>();
        //iteration for g1
        for (V v: g1.vertices()){
            //first occurrence of actors
                freqmap.put(v,1);
        }

        //iteration for g2
        for (V v: g2.vertices()){

            if(!freqmap.containsKey(v)){
                freqmap.put(v,1);
            }
            else{
                freqmap.put(v, freqmap.get(v)+1);
            }
        }
       return freqmap;
    }

    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
        // average Separation= (total path length /number of vertices)
       return totalSep(tree,root,0)/tree.numVertices();
    }

    public static <V,E> double totalSep(Graph<V,E> tree, V root,int num){
        //base case
        if(!tree.inNeighbors(root).iterator().hasNext()) return num;
        double total = num;

        for(V v: tree.inNeighbors(root)){
            //obtaining total pathlength by recursion
            total += totalSep(tree,v,num+1);
        }
        return total;
    }

    public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
        List<V> vertices = new ArrayList<V>((Collection<? extends V>) g.vertices());
        vertices.sort((v1, v2) -> g.inDegree(v2) - g.inDegree(v1));
        return vertices;

    }

}
