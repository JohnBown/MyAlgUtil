package yuanalg;

public class Splay<E extends Comparable<E>> extends BST<E> {

    void attachAsLChild(BinNode p, BinNode lc){//在节点*p与*lc（可能为空）之间建立父（左）子关系
        p.lc = lc; if (lc!=null) lc.parent = p;
    }

    void attachAsRChild(BinNode p, BinNode rc){//在节点*p与*rc（可能为空）之间建立父（右）子关系
        p.rc = rc; if (rc!=null) rc.parent = p;
    }

    /**
     * Splay树伸展算法：从节点v出发逐层伸展
     * @param v
     * @return
     */
    BinNode splay(BinNode v){//将v伸展至根

        if (v==null) return null; BinNode p; BinNode g;////*v的父亲与祖父
        while (((p=v.parent) !=null)&&((g=p.parent) !=null)){//自下而上，反复对*v做双层伸展
            BinNode gg = g.parent;//每轮之后*v都以原曾祖父（great-grand parent）为父
            if (v.lc!=null){
                if (p.lc!=null){//zig-zig
                    attachAsLChild(g,p.lc); attachAsLChild(p,v.rc);
                    attachAsRChild(p,g); attachAsRChild(v,p);
                }else {//zig-zag
                    attachAsLChild ( p, v.rc ); attachAsRChild ( g, v.lc );
                    attachAsLChild ( v, g ); attachAsRChild ( v, p );
                }
            }else if (p.rc!=null){//zag-zag
                attachAsRChild ( g, p.lc ); attachAsRChild ( p, v.lc );
                attachAsLChild ( p, g ); attachAsLChild ( v, p );
            }else {//zag-zig
                attachAsRChild ( p, v.lc ); attachAsLChild ( g, v.rc );
                attachAsRChild ( v, g ); attachAsLChild ( v, p );
            }
            if (gg==null) v.parent = null;//若*v原先的曾祖父*gg不存在，则*v现在应为树根
            else {//否则，*gg此后应该以*v作为左或右孩子
                if ((g == gg.lc)) {
                    attachAsLChild(gg, v);
                } else {
                    attachAsRChild(gg, v);
                }
            }
            updateHeight(g);
            updateHeight(p);
            updateHeight(v);
        }//双层伸展结束时，必有g == NULL，但p可能非空

        if((p=v.parent)!=null){//若p果真非空，则额外再做一次单旋
            if (v.lc!=null){
                attachAsLChild(p,v.rc);
                attachAsRChild(v,p);
            }else {
                attachAsRChild(p,v.lc);
                attachAsLChild(v,p);
            }
            updateHeight(p);
            updateHeight(v);
        }
        v.parent = null;
        return v;
    }//调整之后新树根应为被伸展的节点，故返回该节点的位置以便上层函数更新树根

    //伸展树的查找也会引起整树的结构调整，故search()也需要重写
    BinNode search(E e){//查找
        //调用标准BST的内部接口定位目标节点
        BinNode p = searchIn(root,e,hot=null);
        //无论成功与否，最后被访问的节点都将伸展至根
        root = splay(p!=null?p:hot);
        //总是返回根节点
        return root;
    }

    /**
     * 将关键码e插入伸展树中
     * @param e
     * @return
     */
    BinNode insert(E e){//插入
        if (root==null){//处理原树为空的退化情况
            size++;
            return root = new BinNode(e,root);
        }
        if (e==search(e).data) return root;//确认目标节点不存在
        size++; BinNode t = root;//创建新节点。以下调整<=7个指针以完成局部重构
        if (e.compareTo((E)root.data)>=0){//插入新根，以t和t->rc为左、右孩子
            t.parent = root = new BinNode(e,null);
            root.lc = t; root.rc = t.rc;//2 + 3个
            if (t.rc!=null){//<= 2个
                t.rc.parent = root;
                t.rc = null;
            }
        }else {
            t.parent = root = new BinNode(e,null);
            root.lc = t.lc; root.rc = t;//2 + 3个
            if (t.rc!=null){//<= 2个
                t.lc.parent = root;
                t.lc = null;
            }
        }
        updateHeightAbove(t);//更新t及其祖先（实际上只有_root一个）的高度
        return root;//新节点必然置于树根，返回之
    }//无论e是否存在于原树中，返回时总有_root->data == e

    /**
     * 从伸展树中删除关键码e
     * @param e
     * @return
     */
    boolean remove(E e){//删除
        if (root==null || (e!=search(e).data)) return false;//若树空或目标不存在，则无法删除
        BinNode w = root;//assert: 经search()后节点e已被伸展至树根
        if (root.lc!=null) {//若无左子树，则直接删除
            root = root.rc; if (root != null) root.parent = null;
        } else if (root.rc!=null){//若无右子树，也直接删除
            root = root.lc; if (root != null) root.parent = null;
        }else {//若左右子树同时存在，则
            BinNode lTree = root.lc;
            lTree.parent = null; root.lc = null;//暂时将左子树切除
            root = root.rc; root.parent = null;//只保留右子树
            search((E)w.data);//以原树根为目标，做一次（必定失败的）查找
            // assert: 至此，右子树中最小节点必伸展至根，且（因无雷同节点）其左子树必空，于是
            root.lc = lTree; lTree.parent = root;//只需将原左子树接回原位即可
        }
        if (root!=null) updateHeight(root); //此后，若树非空，则树根的高度需要更新
        return true;//返回成功标志
    }//若目标节点存在且被删除，返回true；否则返回false

}
