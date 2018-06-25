package yuanalg;

public class Stack<E extends Comparable<E>> extends Vector<E> {

    //size()、empty()以及其它开放接口均可直接沿用

    public void push(E e){//入栈
        insert(size,e);
    }

    E pop(){//出栈
        return remove(size()-1);
    }

    E top(){//取顶
        return this.get(size-1);
    }
}
