package yuanalg;


import sun.security.krb5.internal.crypto.EType;

import java.util.Stack;

enum VStatus{UNDISCOVERED, DISCOVERED, VISITED}
enum EStatus{UNDETERMINED, TREE, CROSS, FORWARD, BACKWARD}

public abstract class Graph<Tv,Te> {

    private void reset(){
    }

    int n;//顶点总数
    abstract int insert(Tv tv);//插入顶点，返回编号
    abstract Tv remove(int id);//删除顶点及其关联边，返回该顶点信息
    abstract Tv vertex(int id);//顶点v的数据（该顶点的确存在）
    abstract int inDegree(int id);//顶点v的入度（该顶点的确存在）
    abstract int outDegree(int id);//顶点v的出度（该顶点的确存在）
    abstract int firstNbr(int id);//顶点v的首个邻接顶点
    abstract int nextNbr(int id1, int id2);//顶点v的（相对于顶点j的）下一邻接顶点
    abstract VStatus status(int id);//顶点v的状态
    abstract int dTime(int id);//顶点v的时间标签dTime
    abstract int fTime(int id);//顶点v的时间标签fTime
    abstract int parent(int id);//顶点v在遍历树中的父亲
    abstract int priority(int id);//顶点v在遍历树中的优先级数

    int e;//边总数
    abstract boolean exists(int id1, int id2);//边(v, u)是否存在
    abstract void insert(Te te, int id1, int id2, int id3);//在顶点v和u之间插入权重为w的边e
    abstract Te remove(int id1, int id2);//删除顶点v和u之间的边e，返回该边信息
    abstract EType type(int id1, int id2);//边(v, u)的类型
    abstract Te edge(int id1, int id2);//边(v, u)的数据（该边的确存在）
    abstract int weight(int id1, int id2);//边(v, u)的权重
    abstract EStatus status(int id1,int id2);//边(v, u)的状态

    abstract void bfs(int id);//广度优先搜索算法
    abstract void dfs(int id);//深度优先搜索算法
    abstract void bcc(int id);//基于DFS的双连通分量分解算法
    abstract Stack<Tv> tSort(int id);//基于DFS的拓扑排序算法
    abstract void prim(int id);//最小支撑树Prim算法
    abstract void dijistra(int id);//最短路径Dijkstra算法

}
