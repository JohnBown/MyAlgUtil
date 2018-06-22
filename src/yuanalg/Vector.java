package yuanalg;

import java.util.Random;

public class Vector<E extends Comparable<E>> {

    final int DEFAULT_CAPACITY = 3;//默认初始容量（实际应用中可设为更大）

    static Object[] elem;//数据区

    static int size;//规模

    static int capacity;//容量

    public Vector(){
        elem = new Object[capacity = DEFAULT_CAPACITY];
        for (size = 0; size<capacity;elem[size++] = 0);
    }

    public Vector(E[] A, int n){
        copyFrom(A,0,n);
    }

    public Vector(E[] A, int lo, int hi){
        copyFrom(A,lo,hi);
    }

    public Vector(Vector V){
        copyFrom((E[])V.elem,0,V.size);
    }

    public Vector(Vector V, int lo, int hi){
        copyFrom((E[])V.elem,lo,hi);
    }

    /**
     * 报告向量当前的规模（元素总数）
     * @return
     */
    public int size(){
        return size;
    }

    /**
     * 获取秩为r的元素
     * @return
     */
    public E get(int r){
        return (E)elem[r];
    }

    /**
     * 用e替换秩为r的元素的值
     * @param r
     * @param e
     * @return
     */
    public boolean put(int r, E e){
        if (size<r) return false;
        elem[r] = e;
        return true;
    }

    /**
     * 判空
     * @return
     */
    public boolean empty(){
        return size!=0;
    }

    /**
     * 判断向量是否已排序
     * @return
     */
    public int disordered(){//统计向量中的逆序相邻元素对
        int n = 0;//计数器
        for (int i = 1; i < size; i++) {//逐一检查各对相邻元素
            if(((E)elem[i-1]).compareTo((E)elem[i])>0) n++;//逆序则计数
        }
        return n;//向量有序当且仅当n=0
    }//若只需判断是否有序，则首次遇到逆序对之后，即可立即终止

    /**
     * 在命中多个元素时可返回秩最大者
     * @param e
     * @return
     */
    public int find(E e){
        return find(e, 0, size);
    }

    /**
     * 在区间[lo,hi)中查找元素
     * @param e
     * @param lo
     * @param hi
     * @return
     */
    public int find(E e, int lo, int hi){//0<=lo<hi<=size
        while ((lo<hi--) && (e!=elem[hi]));//逆向查找
        return hi;//hi<lo 意味着失败，否则hi即命中元素的秩
    }

    /**
     * 有序向量整体查找
     * @param e
     * @return
     */
    public int search(E e){
        return (0>= size)? -1: search(e,0,size);
    }

    /**
     * 有序向量区间查找
     * @param e
     * @param lo
     * @param hi
     * @return
     */
    public int search(E e, int lo, int hi){
        Random rand = new Random();
        return (rand.nextBoolean())//各按50%概率随机选用
                ? binSearch((E[])elem,e,lo,hi) //二分查找算法，或者
                : fibSearch((E[])elem,e,lo,hi);//Fibonacci查找算法
    }

    public int binSearch(E[] S, E e, int lo, int hi){
        while (lo<hi){//不变性：A[0,li) <= e < A[hi,n)
            int mi = (lo+hi) >> 1;//以中点为轴点，经比较后确定深入
            if (e.compareTo(S[mi])<0) hi = mi;//[lo,mi)或[mi,hi)
            else lo = mi+1;
        }//出口时hi=lo+1，查找区间仅含一个元素A[lo]
        return --lo;//返回命中元素的秩或-1
    }

    public int fibSearch(E[] S, E e, int lo, int hi){
        Fib fib = new  Fib(hi - lo);//用O(log_phi(n = hi - lo)时间创建Fib数列
        while (lo<hi){//每步迭代可能要做两次比较判断，有三个分支
            while (hi - lo<fib.get()) fib.prev();//通过向前顺序查找（分摊O(1)）——至多迭代几次？
            int mi = lo + fib.get() - 1;//确定形如Fib(k) - 1的轴点
            if (e.compareTo(S[mi])<0) hi = mi;//深入前半段[lo, mi)继续查找
            else if (S[mi].compareTo(e)<0) lo = mi +1;//深入后半段(mi, hi)继续查找
            else  return mi;//在mi处命中
        }//成功查找可以提前终止
        return -1;//查找失败
    }//有多个命中元素时，不能保证返回秩最大者；失败时，简单地返回-1，而不能指示失败的位置

    /**
     * 删除秩为r的元素
     * @param r
     * @return
     */
    public E remove(int r){
        Object e = elem[r];//备份被删除元素
        remove(r,r+1);//调用区间删除算法
        return (E)e;//返回被删除元素
    }

    /**
     * 删除秩在区间[lo, hi)之内的元素
     * @param lo
     * @param hi
     * @return
     */
    public int remove(int lo, int hi){//删除区间[lo,hi)
        //assert: 0<=lo<=hi<=size
        if (lo == hi) return 0; //出于效率考虑，单独处理退化情况
        while (hi < size) elem[lo++] = elem[hi++]; //[hi,size)顺次前移
        size = lo; shrink();//更新规模，若有必要则缩容
        return hi - lo;//返回被删除的元素个数
    }

    /**
     * 插入元素
     * @param r
     * @param e
     * @return
     */
    public int insert(int r, E e){//将e作为秩为r元素插入
        //assert: 0 <= r <= size
        expand();//若有必要，扩容
        for (int i = size; i >r ; i--) //自后向前
            elem[i] = elem[i-1]; //后继元素顺次后移一个单元
        elem[r] = e; size++; //置入新元素，更新容量
        return r;//返回秩
    }

    /**
     * 默认作为末元素插入
     * @param e
     * @return
     */
    public int insert(E e){
        return insert(size,e);
    }

    /**
     * 对[lo, hi)排序
     * @param lo
     * @param hi
     */
    public void sort(int lo, int hi){

    }

    /**
     * 整体排序
     */
    public void sort(){
        sort(0,size);
    }

    /**
     * 对[lo, hi)置乱
     * @param lo
     * @param hi
     */
    public void unsort(int lo, int hi){

    }

    /**
     * 整体置乱
     */
    public void unsort(){
        unsort(0, size);
    }

    /**
     * 无序去重
     * @return
     */
    public int deduplicate(){
        int oldSize = size;//记录原规模
        int i = 1;//从elem[1]开始
        while (i<size){//自前向后逐一考查各元素elem[i]
            if ((find((E) elem[i], 0, i) < 0)) i++;//在其前缀中寻找与之雷同者（至多一个）
            else remove(i);//若无雷同则继续考查其后继，否则删除雷同者
        }
        return oldSize - size;//向量规模变化量，即被删除元素总数
    }

    /**
     * 有序去重
     * @return
     */
    public int uniquify(){
        int i = 0, j = 0;//各对互异“相邻”元素的秩
        while (++j<size)//逐一扫描，直至末元素
            //跳过雷同者，发现不同元素时，向前移至紧邻于前者右侧
            if (elem[i]!=elem[j]) elem[++i] = elem[j];
        size = ++i; shrink();//直接截除尾部多余元素
        return j - i;//向量规模变化量，即被删除元素总数
    }//注意：通过remove(lo,hi)批量删除，依然不能达到高效率

    /**
     * 复制数组区间A[lo, hi)
     * @param A
     * @param lo
     * @param hi
     */
    void copyFrom (E[] A, int lo, int hi){
        elem = new Object[capacity = 2*(hi - lo)];//分配空间
        size = 0;//规模清零
        while (lo<hi)//A[lo,hi)内元素逐一
            elem[size++] = A[lo++];//复制至elem[0,hi-lo)
    }

    /**
     * 空间不足时扩容
     */
    void expand(){
        if (size<capacity) return;//尚未满员时，不必扩容
        capacity = capacity<DEFAULT_CAPACITY?DEFAULT_CAPACITY:capacity;//不低于最小容量
        Object[] oldElem = elem;
        elem = new Object[capacity<<1];//容量加倍
        for (int i = 0; i < size; i++) {//复制原向量内容
            elem[i] = oldElem[i];
        }
    }

    /**
     * 装填因子过小时压缩
     */
    void shrink(){

    }

    /**
     * 扫描交换
     * @param lo
     * @param hi
     * @return
     */
    boolean bubble(int lo, int hi){
        return false;
    }

    /**
     *
     * @param lo
     * @param hi
     * @return
     */
    boolean bubbleSort(int lo, int hi){
        return false;
    }

    /**
     * 选取最大元素
     * @param lo
     * @param hi
     * @return
     */
    int max(int lo, int hi){
        Object max = elem[lo];
        int maxrank;
        for (maxrank = lo;lo<=hi;lo++){
            if (((E)max).compareTo((E)elem[lo])<0) {
                max = elem[lo];
                maxrank = lo;
            }
        }
        return maxrank;
    }

    /**
     * 选择排序算法
     * @param lo
     * @param hi
     */
    void selectionSort(int lo, int hi){

    }

    /**
     * 归并算法
     * @param lo
     * @param mi
     * @param hi
     */
    void merge(int lo, int mi, int hi){

    }

    /**
     * 归并排序算法
     * @param lo
     * @param hi
     */
    void mergeSort(int lo, int hi){

    }

    /**
     * 轴点构造算法
     * @param lo
     * @param hi
     * @return
     */
    int partition(int lo, int hi){
        return -1;
    }

    /**
     * 快速排序算法
     * @param lo
     * @param hi
     */
    void quickSort(int lo, int hi){

    }

    /**
     * 堆排序（稍后结合完全堆讲解）
     * @param lo
     * @param hi
     */
    void heapSort(int lo, int hi){

    }
}
