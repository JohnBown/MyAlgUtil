package yuanalg;

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


}
