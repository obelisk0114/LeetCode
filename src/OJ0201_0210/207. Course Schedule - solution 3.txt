class GNode {
  public Integer inDegrees = 0;
  public List<Integer> outNodes = new LinkedList<Integer>();
}


class Solution {

  public boolean canFinish(int numCourses, int[][] prerequisites) {

    if (prerequisites.length == 0)
      return true; // no cycle could be formed in empty graph.

    // course -> list of next courses
    HashMap<Integer, GNode> graph = new HashMap<>();

    // build the graph first
    for (int[] relation : prerequisites) {
      // relation[1] -> relation[0]
      GNode prevCourse = this.getCreateGNode(graph, relation[1]);
      GNode nextCourse = this.getCreateGNode(graph, relation[0]);

      prevCourse.outNodes.add(relation[0]);
      nextCourse.inDegrees += 1;
    }

    // We start from courses that have no prerequisites.
    int totalDeps = prerequisites.length;
    LinkedList<Integer> nodepCourses = new LinkedList<Integer>();
    for (Map.Entry<Integer, GNode> entry : graph.entrySet()) {
      GNode node = entry.getValue();
      if (node.inDegrees == 0)
        nodepCourses.add(entry.getKey());
    }

    int removedEdges = 0;
    while (nodepCourses.size() > 0) {
      Integer course = nodepCourses.pop();

      for (Integer nextCourse : graph.get(course).outNodes) {
        GNode childNode = graph.get(nextCourse);
        childNode.inDegrees -= 1;
        removedEdges += 1;
        if (childNode.inDegrees == 0)
          nodepCourses.add(nextCourse);
      }
    }

    if (removedEdges != totalDeps)
      // if there are still some edges left, then there exist some cycles
      // Due to the dead-lock (dependencies), we cannot remove the cyclic edges
      return false;
    else
      return true;
  }

  /**
   * Retrieve the existing <key, value> from graph, otherwise create a new one.
   */
  protected GNode getCreateGNode(HashMap<Integer, GNode> graph, Integer course) {
    GNode node = null;
    if (graph.containsKey(course)) {
      node = graph.get(course);
    } else {
      node = new GNode();
      graph.put(course, node);
    }
    return node;
  }
}