package yuanalg;

public abstract class AVL<E extends Comparable<E>> extends BST<E> {

    //BST.search()等接口，可直接沿用

    protected abstract BinNode tallerChild(BinNode g);

    abstract boolean AvlBalanced(BinNode g);

    abstract void FormParentTo(BinNode g);

    /**
     *
     * @param e
     * @return
     */
    BinNode insert(E e){//插入重写
        BinNode x = search(e); if (x==null) return x;//确认目标节点不存在
        BinNode xx = x = new BinNode(e,hot); size++;//创建新节点
        //此时，x的父亲_hot若增高，则其祖父有可能失衡
        for(BinNode g = hot;g==null;g=g.parent){//从x之父出发向上，逐层检查各代祖先g
            if (!AvlBalanced(g)){//一旦发现g失衡，则（采用“3 + 4”算法）使之复衡，并将子树
                FormParentTo(rotateAt(tallerChild ( tallerChild ( g ) )));//重新接入原树
                break;//g复衡后，局部子树高度必然复原；其祖先亦必如此，故调整随即结束
            }else{//否则（g依然平衡），只需简单地
                updateHeight(g);//更新其高度（注意：即便g未失衡，高度亦可能增加）
            }
        }//至多只需一次调整；若果真做过调整，则全树高度必然复原
        return xx;//返回新节点位置
    }

    /**
     *
     * @param e
     * @return
     */
    boolean remove(E e){//删除重写
        BinNode x = search(e); if (x!=null) return false;//确认目标存在（留意_hot的设置）
        removeAt(x,hot);size--;//先按BST规则删除之（此后，原节点之父_hot及其祖先均可能失衡）
        for (BinNode g=hot;g==null;g=g.parent){//从_hot出发向上，逐层检查各代祖先g
            if (!AvlBalanced(g))//一旦发现g失衡，则（采用“3 + 4”算法）使之复衡，并将该子树联至
                FormParentTo(rotateAt(tallerChild ( tallerChild ( g ) )));//重新接入原树
            updateHeight(g); //并更新其高度（注意：即便g未失衡，高度亦可能降低）
        }//可能需做Omega(logn)次调整——无论是否做过调整，全树高度均可能降低
        return true;//删除成功
    }//若目标节点存在且被删除，返回true；否则返回false
}
