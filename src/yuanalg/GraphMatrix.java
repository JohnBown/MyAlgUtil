package yuanalg;

import sun.security.krb5.internal.crypto.EType;

import java.util.Stack;
import java.util.Vector;

public class GraphMatrix<Tv,Te> extends Graph<Tv,Te>{

    class Vertex<Tv> {//顶点对象（并未严格封装）

        Tv data; int inDegree, outDegree; VStatus status;//数据、出入度数、(如上三种)状态
        int dTime, fTime;//时间标签
        int parent;//在遍历树中的父节点
        int priority;//在遍历树中的优先级（最短通路、极短跨边等）

        Vertex(){}
        Vertex(Tv e){
            data = e; inDegree = 0; outDegree = 0; status = VStatus.UNDISCOVERED;
            dTime = -1; fTime = -1; parent = -1; priority = Integer.MAX_VALUE;
        }
    }

    class Edge<Te> {//边对象（未严格封装）

        Te data; int weight; EStatus status;//数据、权重、类型
        Edge(Te d, int w){//构造新边
            data = d; weight = w; status = EStatus.UNDETERMINED;
        }
    }

    Vector<Vertex<Tv>> V;//顶点集（向量）
    Vector<Vector<Edge<Te>>> E;//边集（邻接矩阵）

    GraphMatrix(){
        n = e = 0;//构造
    }


    /**
     * 插入顶点：
     * 1、将邻接矩阵中已有的各行分别拓展1个单元
     * 2、针对新引入的顶点，为邻接矩阵增加1行
     * 3、为二维边表的1级边表增加1个单元
     * 4、对应新引入顶点，为顶点向量增加1个单元
     * @param tv
     * @return
     */
    @Override
    int insert(Tv tv) {//插入顶点，返回编号
        for (int i = 0; i < n; i++) E.add(i,null);//[1]
        n++;
        E.add(new Vector<>(n));//[2][3]
        return V.add(new Vertex<>())?n+1:-1;//[4]
    }

    /**
     * 删除顶点
     * @param i
     * @return
     */
    @Override
    Tv remove(int i) {//删除顶点及其关联边，返回该顶点信息
        for (int j = 0; j < n; j++) {
            if (exists(i,j)){//删除所有出边
                E.get(i).remove(j);
                V.get(j).inDegree--;
            }
        }
        E.remove(i);n--;//删除第i行
        for (int j = 0; j < n; j++) {
            if (exists(j,i)){//删除所有入边及第i列
                E.get(j).remove(i);
                V.get(j).outDegree--;
            }
        }
        Tv vBak = vertex(i);//备份顶点i的信息
        V.remove(i);//删除顶点i
        return vBak;//返回被删除顶点的信息
    }

    @Override
    Tv vertex(int i) {//数据
        return V.get(i).data;
    }

    @Override
    int inDegree(int i) {//入度
        return V.get(i).inDegree;
    }

    @Override
    int outDegree(int i) {//出度
        return V.get(i).outDegree;
    }

    @Override
    int firstNbr(int i) {//首个邻接顶点
        return nextNbr(i,n);
    }

    @Override
    int nextNbr(int i, int j) {//相对于顶点j的下一邻接顶点（改用邻接表可提高效率）
        while ((-1<j)&&(!exists(i,--j)));//逆向线性试探
        return j;
    }

    @Override
    VStatus status(int i) {//状态
        return V.get(i).status;
    }

    @Override
    int dTime(int i) {//时间标签dTime
        return V.get(i).dTime;
    }

    @Override
    int fTime(int i) {//时间标签fTime
        return V.get(i).fTime;
    }

    @Override
    int parent(int i) {//在遍历树中的父亲
        return V.get(i).parent;
    }

    @Override
    int priority(int i) {//在遍历树中的优先级数
        return V.get(i).priority;
    }

    @Override
    boolean exists(int i, int j) {//判断i，j是否存在
        return (0<=i) && (i<n) && (0<=j) && (j<n)
                && E.get(i).get(j)!=null;//短路求值
    }

    /**
     * 插入(i,j,w)
     * @param edge
     * @param w
     * @param i
     * @param j
     */
    @Override
    void insert(Te edge, int w, int i, int j) {
        if (exists(i,j)) return;//忽略已有的边
        E.get(i).add(j, new Edge<Te>(edge,w));//创建新边
        e++;//更新边计数
        V.get(i).outDegree++;//更新关联顶点i的出度
        V.get(j).inDegree++;//更新关联顶点j的入度
    }

    /**
     * 删除顶点i和j之间的联边(exists(i,j))
     * @param i
     * @param j
     * @return
     */
    @Override
    Te remove(int i, int j) {
        Te eBak = edge(i,j);//备份边（i，j）的信息
        E.get(i).remove(j);//删除边（i，j）
        e--;//更新边计数
        V.get(i).outDegree--;//更新关联顶点i的出度
        V.get(j).inDegree--;//更新关联顶点j的入度
        return eBak;
    }

    @Override
    EType type(int id1, int id2) {
        return null;
    }

    @Override
    Te edge(int i, int j) {//边（i，j）的数据
        return E.get(i).get(j).data;
    }

    @Override
    EStatus status(int i, int j) {//边（i，j）的状态
        return E.get(i).get(j).status;
    }

    @Override
    int weight(int i, int j) {//边（i，j）的权重
        return E.get(i).get(j).weight;
    }

    @Override
    void bfs(int id) {

    }

    @Override
    void dfs(int id) {

    }

    @Override
    void bcc(int id) {

    }

    @Override
    Stack<Tv> tSort(int id) {
        return null;
    }

    @Override
    void prim(int id) {

    }

    @Override
    void dijistra(int id) {

    }
}
