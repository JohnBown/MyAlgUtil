package yuanalg;

import java.util.Queue;
import java.util.Stack;

public class BinTree<E extends Comparable<E>> {

    protected class BinNode<E>{
        BinNode parent, lc, rc;//父亲，孩子
        E data;//数据域
        int height;//高度

        public BinNode(E data, BinNode parent) {
            this.data = data;
            this.parent = parent;
        }

        BinNode insertAsLC(E e){//作为左孩子插入新节点
            return lc = new BinNode(e,this);
        }

        BinNode insertAsRC(E e){//作为右孩子插入新节点
            return rc = new BinNode(e, this);
        }

        /**
         * 后代总数，亦即以其为根的子树的规模
         * @return
         */
        int size(){//子树规模
            int s = 1;//计入本身
            if (lc!=null) s += lc.size();//递归计入左子树规模
            if (rc!=null) s += rc.size();//递归计入右子树规模
            return s;
        }

        BinNode succ(){//（中序遍历意义下）当前节点的直接后继
            return null;
        }

        void travLevel(){//子树层次遍历

        }

        void travPre(){//子树先序遍历

        }

        void travIn(){//子树中序遍历

        }

        void travPost(){//子树后序遍历

        }
    }

    private int size;//规模
    private BinNode root;//根节点

    private int stature(BinNode p){//节点高度，约定空树高度为-1
        return p!=null?p.height:-1;
    }

    protected int updateHeight(BinNode x){//更新节点x的高度，具体规则因树不同而异
        return x.height = 1 + Math.max(stature(x.lc),stature(x.rc));
    }

    protected void updateHeightAbove(BinNode x){//更新x及祖先的高度
        while (x!=null){//可优化：一旦高度未变，即可终止
            updateHeight(x);
            x = x.parent;
        }
    }

    public int size(){//规模
        return size;
    }

    public boolean empty(){//判空
        return root==null;
    }

    BinNode root(){//树根
        return root;
    }

    BinNode insertAsRoot(E e){//插入根节点
        this.root.data = e;
        return root;
    }

    BinNode insertAsLC(BinNode x, E e){//e作为x的左孩子（原无）插入
        size++;
        x.insertAsLC(e);//x祖先的高度可能增加，其余节点必然不变
        updateHeightAbove(x);
        return x.lc;
    }

    BinNode insertAsRC(BinNode x, E e){//e作为x的右孩子（原无）插入
        size++;
        x.insertAsRC(e);//x祖先的高度可能增加，其余节点必然不变
        updateHeightAbove(x);
        return x.rc;
    }

    BinNode attachAsLC(BinNode x, BinTree S){//S作为x左子树接入
        return null;
    }

    BinNode attachAsRC(BinNode x, BinTree S){//S作为x右子树接入
        return null;
    }

    int remove (BinNode x ) {//删除以位置x处节点为根的子树，返回该子树原先的规模
        return -1;
    }

    void traverse(BinNode x){//递归形式

        /*递归形式先序遍历*/
//        if (x==null) return;
//        访问x.data
//        traverse(x.lc);
//        traverse(x.rc);

        /*迭代形式先序遍历*/
//        Stack<BinNode> S = null;//辅助栈
//        S.push(x);//根节点入栈
//        while (!S.empty()){//在栈变空之前反复循环
//            x = S.pop();//弹出并访问当前节点
//            访问x.data
//            if (x.rc!=null) S.push(x.rc);//右孩子先入后出
//            else if (x.lc!=null) S.push(x.lc);//左孩子后入先出
//        }
    }

    void visit(E e) {
        System.out.print(e+"");
    }

    void visitAlongVine(BinNode x, Stack<BinNode> S){
        while (x!=null){//反复地
            visit((E)x.data);//访问当前节点
            S.push(x.rc);//右孩子（右子树）入栈（将来逆序出栈）
            x = x.lc;//沿左侧链下行
        }//只有右孩子，NULL可能入栈
    }

    /**
     * 先序遍历
     * @param x
     */
    void travPre(BinNode x){
        Stack<BinNode> S = null;//辅助栈
        while (true){//以（右）子树为单位，逐批访问节点
            visitAlongVine(x,S);//访问子树x的左侧链，右子树入栈缓冲
            if (S.empty()) break;//栈空即退出
            x = S.pop();//弹出下一子树的根
        }
    }

    void goAlongVine(BinNode x, Stack<BinNode> S){
        while (x!=null){
            S.push(x);
            x = x.lc;
        }
    }//反复地入栈，沿左分支深入

    /**
     * 中序遍历
     * @param x
     */
    void travIn(BinNode x){
        Stack<BinNode> S = null;//辅助栈
        while (true){//反复地
            goAlongVine(x,S);//从当前节点出发，逐批入栈
            if (S.empty()) break;//直至所有节点处理完毕
            x = S.pop();//x的左子树或为空，或已遍历（等效于空），故可以
            visit((E)x.data);//立即访问之
            x = x.rc;//再转向其右子树（可能为空，留意处理手法）
        }
    }

    /**
     * 层次遍历
     */
    void travLevel(){//二叉树层次遍历
        Queue<BinNode> Q = null;//引入辅助队列
        Q.add(this.root);//根节点入队
        while (!Q.isEmpty()){//在队列再次变空之前，反复迭代
            BinNode x = Q.poll();//取出队首节点，并随即
            visit((E)x.data);//访问之
            if (x.lc!=null) Q.add(x.lc);//左孩子入队
            if (x.rc!=null) Q.add(x.rc);//右孩子入队
        }
    }

}
