package yuanalg;

public abstract class BST<E extends Comparable<E>> extends BinTree<E>{

    /**
     *
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

    abstract BinNode insert(E e);//插入

    abstract boolean remove(E e);//删除

    BinNode hot;//命中节点的父亲

    abstract BinNode connect34(//3+4重构
            BinNode n1, BinNode n2, BinNode n3,
            BinNode n4, BinNode n5, BinNode n6, BinNode n7
    );

    abstract BinNode rotateAt(BinNode n);//旋转调整
}
