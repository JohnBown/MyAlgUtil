package yuanalg;

public abstract class BST<E extends Comparable<E>> extends BinTree<E>{

    /**
     * 二叉搜索树查找
     * @param e
     * @return
     */
    BinNode search(E e){//从根节点启动查找
        return searchIn(root(),e, hot);
    }//查找

    BinNode searchIn(//典型的尾递归，可以改为迭代版
            BinNode v, //当前（子）树根
            E e,//目标关键码
            BinNode hot){//记忆热点
        if (v==null||(e.compareTo((E)v.data)==0)) return v;//足以确定失败、成功，或者
        hot = v;//先记下当前（非空）节点，然后再...
        return searchIn((e.compareTo((E)v.data)<0?v.lc:v.rc),e,hot);
    }

    /**
     * 二叉搜索树插入
     * @param e
     * @return
     */
    BinNode insert(E e) {//插入
        BinNode x = search(e);//查找目标（留意hot的设置）
        if (x==null){//即禁止雷同元素，故仅在查找失败时才实施插入操作
            x = new BinNode(e,hot);//在x处创建新节点，以hot为父亲
            size++; updateHeightAbove(x);//更新全树规模，更新x及其历代祖先的高度
        }
        return x;//无论e是否存在于原树，至此总有x.data == e
    }

    /**
     * 二叉搜索树删除
     * @param e
     * @return
     */
    boolean remove(E e) {//删除
        BinNode x = search(e);//查找目标（留意hot的设置）
        if (x==null) return false;//确认目标存在（此时hot为x的父亲）
        removeAt(x, hot);//分两大类情况实施删除，更新全树规模
        size--;//更新全树规模
        updateHeightAbove(hot);//更新hot及其历代祖先的高度
        return true;
    }//删除与否由返回值指示

    BinNode removeAt(BinNode x, BinNode hot){
        BinNode w = x;//实际被摘除的节点，初值同x
        BinNode succ = null;//实际被删除节点的接替者
        if (x.lc==null)//若*x的左子树为空，则可
            succ = x = x.rc;//直接将*x替换为其右子树
        else if (x.rc==null)//若右子树为空，则可
            succ = x = x.lc;//对称地处理——注意：此时succ != NULL
        else {//若左右子树均存在，则选择x的直接后继作为实际被摘除节点，为此需要
            w = w.succ();//（在右子树中）找到*x的直接后继*w
            E temp = (E)w.data;//交换*x和*w的数据元素
            w.data = x.data;
            x.data = temp;
            BinNode u = w.parent;
            if (u==x){//隔离节点*w
                u.rc = succ = w.rc;
            }else {
                u.lc = succ = w.rc;
            }
        }
        hot = w.parent; //记录实际被删除节点的父亲
        if (succ!=null) succ.parent = hot;//并将被删除节点的接替者与hot相联
        return succ;//释放被摘除节点，返回接替者
    }

    BinNode hot;//命中节点的父亲

    abstract BinNode connect34(//3+4重构
            BinNode n1, BinNode n2, BinNode n3,
            BinNode n4, BinNode n5, BinNode n6, BinNode n7
    );

    abstract BinNode rotateAt(BinNode n);//旋转调整
}
