package yuanalg;

public class List<E extends Comparable<E>> {
    protected class ListNode<E>{
        E data;//数值
        ListNode pred,succ;//前驱、后继

        ListNode(){}//针对header和trailer的构造器

        public ListNode(E data, ListNode pred, ListNode succ) {//默认构造器
            this.data = data;
            this.pred = pred;
            this.succ = succ;
        }

        ListNode insertAsPred(E e){//紧靠当前节点之前插入新节点

            ListNode x = new ListNode(e,pred,this);
            pred.succ = x; pred = x;
            return x;//返回新节点的位置
        }

        ListNode insertAsSucc(E e){//紧随当前节点之后插入新节点

            ListNode x = new ListNode(e,this,succ);
            succ.pred = x; succ = x;
            return x;
        }
    }

    private int size;//规模
    private ListNode header,trailer;//头哨兵、尾哨兵

    public void init() {
        header = new ListNode<E>();//创建头哨兵节点
        trailer = new ListNode<E>();//创建尾哨兵节点

        //互联
        header.succ = trailer;
        header.pred = null;
        trailer.pred = header;
        trailer.succ = null;

        size = 0;//记录规模
    }

    /**
     * 复制列表中自位置p起的n项
     * @param p
     * @param n
     */
    public void copyNodes(ListNode p, int n){
        init();//创建头、尾哨兵节点并做初始化
        while (n-->0){//将起自p的n项依次作为末节点插入
            insertAsLast((E)p.data);
            p = p.succ;
        }
    }

    public List(){//默认
        init();
    }

    public List(List L){}//整体复制列表L

    public List(List L, int r, int n){}//复制列表L中自第r项起的n项

    public List(ListNode p, int n){}//复制列表中自位置p起的n项

    public int size(){//规模
        return size;
    }

    public boolean empty(){//判空
        return size<=0;
    }

    public E getItem(int r){//assert: 0 <= r <size
        ListNode p = header.succ;//从首结点出发
        while (0<r--) p = p.succ;//顺数第r个结点即是
        return (E) p.data;//目标节点
    }//任一节点的秩，亦即其前驱的总数

    public ListNode first(){//首节点位置
        return header.succ;
    }

    public ListNode last(){//末节点位置
        return trailer.pred;
    }

    public boolean valid(ListNode p){//判断位置p是否对外合法
        return (p!=null) && (trailer!=p) && (header!=p);//将头、尾节点等同于NULL
    }

    public int disordered(){//判断列表是否已排序
        return -1;
    }

    public ListNode find(E e) {//无序列表查找
        return find(e,size,trailer);
    }

    /**
     * p的n个前驱中查找特定的元素e
     * @param e
     * @param n
     * @param p
     * @return
     */
    public ListNode find(E e, int n, ListNode p){//无序区间查找
        while (0<n--)//从右向左，逐个将p的前驱与e比对
            if (e==(p=p.pred).data)return p;//直至命中或范围越界
        return null;//若越出左边界，意味着查找失败
    }//header的存在使得处理更为简洁

    public ListNode search(E e){//有序列表查找
        return search(e,size,trailer);
    }

    /**
     * 在有序列表内节点p的n个（真）前驱中，找到不大于e的最后者
     * @param e
     * @param n
     * @param p
     * @return
     */
    public ListNode search(E e, int n, ListNode p){//有序区间查找
        while (0<=n--)//对于p的最近的n个前驱，从左向右
            if (((E)(p=p.pred).data).compareTo(e)<=0) break;//逐个比较
        return p;//直至命中、数值越界或范围越界后，返回查找终止的位置
    }

    public ListNode selectMax(){//整体最大者
        return selectMax(header.succ,size);
    }

    public ListNode selectMax(ListNode p, int n){//在p及其n-1个后继中选出最大者
        ListNode max = p;//最大者暂定为p
        for (ListNode cur = p;1<n;n--){//后续节点逐一与max比较
            if (((E)(cur=cur.succ).data).compareTo((E)p.data)>=0){//若>=max
                max = cur;//则更新最大元素的位置记录
            }
        }
        return max;//返回最大节点位置
    }

    /**
     * 将e当作首节点插入
     * @param e
     * @return
     */
    public ListNode insertAsFirst(E e){//将e当作首节点插入
        return insertA(header,e);
    }

    /**
     * 将e当作末节点插入
     * @param e
     * @return
     */
    public ListNode insertAsLast(E e){
        return insertB(trailer,e);
    }

    public ListNode insertA(ListNode p, E e){//将e当作p的后继插入（After）
        return p.insertAsSucc(e);
    }

    public ListNode insertB(ListNode p, E e){//将e当作p的前驱插入（Before）
        return p.insertAsPred(e);
    }

    /**
     * 删除合法位置p处的节点,返回被删除节点
     * @param p
     * @return
     */
    public E remove(ListNode p){
        E e = (E) p.data;//备份要删除节点数值（设置类型E可直接赋值）
        p.pred.succ = p.succ;
        p.succ.pred = p.pred;
        size--;
        return e;//返回备份数值
    }

    //TODO
    public void merge(List<E> L){

    }

    /**
     * 剔除无序列表中的重复节点
     * @return
     */
    public int deduplicate(){//无序去重
        if (size<2) return 0;//平凡列表自然无重复
        int oldSize = size;//记录原规模
        ListNode p = first(); int r = 1;//p从首节点起
        while (trailer != (p=p.succ)){//依次直到末节点
            ListNode q = find((E)p.data,r,p);//在p的r个（真）前驱中，查找与之雷同者
            if (q != null) {//若的确存在，则删除之
                remove(q);//这里删除q而不删除p，避免p=p.succ存在风险
            } else {//否则秩递增
                r++;
            }
        }//assert：循环过程中的任意时刻，p的所有前驱互不相同
        return oldSize -size;//列表规模变化量，即被删除元素总数
    }

    /**
     * 成批剔除重复元素
     * @return
     */
    public int uniquify(){//有序去重
        if(size<2) return 0;//平凡列表自然无重复
        int oldSize = size;//记录原规模
        ListNode p = first(); ListNode q;//p为各区段起点，q为其后继
        while (trailer!=(q=p.succ))//反复考察紧邻的节点对(p, q)
            if (p.data != q.data) p = q;//若互异，则转向下一区段
            else remove(q);//否则（雷同），删除后者
        return oldSize - size;//规模变化量，即被删除元素总数
    }

    public void reverse(){//前后倒置（习题）

    }
}
