package yuanalg;

import java.util.Vector;

public class BTree<E extends Comparable<E>> {

    class BTNode<E> {
        BTNode parent;//父
        Vector<E> key;//数值向量
        Vector<BTNode> child;//孩子向量（其长度总比key多一）

        BTNode(){
            parent = null;
            child.add(0,null);
        }

        BTNode(E e, BTNode lc, BTNode rc){
            parent = null;//作为根节点，而且初始时
            key.add(0,e);//仅一个关键码
            child.add(0,lc); child.add(1,lc);//两个孩子
            if (lc!=null) lc.parent = this;
            if (rc!=null) rc.parent = this;
        }
    }

    int size; int order; BTNode root;//关键码总数、阶次、根
    BTNode hot;//search()最后访问的非空节点位置

    void solveOverflow(BTNode t);//因插入而上溢后的分裂处理
    void solveUnderflow(BTNode t);//因删除而下溢后的合并处理

    /**
     *
     * @param e
     * @return
     */
    BTNode search(E e) {//查找
        BTNode v = root; hot = null;//从根节点出发
        while (v!=null){//逐层查找
            int r = v.key.indexOf(e);//在当前节点对应的向量中顺序查找
            if (0<=r && e==v.key.get(r)) return v;//若成功，则返回；否则
            hot = v; v = (BTNode) v.child.get(r+1);//沿引用转至对应的下层子树，并载入其根I/O
        }//若因！v而退出，则意味着抵达外部节点
        return null;//失败
    }

    boolean insert(E e);//插入
    boolean remove(E e);//删除
}
