package yuanalg;

public class Queue<E extends Comparable<E>> extends List<E>{

    //size()与empty()直接沿用

    void enqueue(E e){//入队
        insertAsLast(e);
    }

    E dequeue(){//出队
        return remove(first());
    }

    E front(){//队首
        return (E)first().data;
    }
}
