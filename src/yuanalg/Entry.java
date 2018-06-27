package yuanalg;

public class Entry<K extends Comparable<K>,V> {

    K key; V value; //关键码、数值

    Entry(K k, V v){//默认构造函数
        key = k;
        value = v;
    }

    Entry(Entry<K,V> e){//基于克隆的构造函数
        key = e.key;
        value = e.value;
    }

    boolean l(Entry<K,V> e){//小于
        return key.compareTo(e.key)<0;
    }

    boolean g(Entry<K,V> e){//大于
        return key.compareTo(e.key)>0;
    }

    boolean eq(Entry<K,V> e){//等于
        return key.compareTo(e.key)==0;
    }

    boolean neq(Entry<K,V> e){//不等于
        return key.compareTo(e.key)!=0;
    }
}
