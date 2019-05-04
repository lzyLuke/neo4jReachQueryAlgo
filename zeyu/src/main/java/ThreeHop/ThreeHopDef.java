package ThreeHop;

import java.util.ArrayList;
import java.util.Comparator;

public class ThreeHopDef {
}

class Binfor {
    int rank;
    double dens;
    int bid;
    @Override
    public boolean equals(Object p){
        Binfor lb = (Binfor) p;
        if(this.rank==lb.rank&&this.dens==lb.dens&&this.bid==lb.bid)
            return true;
        else
            return false;
    }
}
class VertexInfor{
    int part;
    int rank;
}
class LabelPair{
    int first;
    int second;
    LabelPair(Integer a,Integer b){
        first=a;
        second=b;
    }
    @Override
    public boolean equals(Object p){
        LabelPair lb = (LabelPair) p;
        if(this.first==lb.first&&this.second==lb.second)
            return true;
        else
            return false;
    }
}
class Interval{
    int begin;
    int end;
    int id;
    int cid;
    @Override
    public boolean equals(Object p){
        Interval lb = (Interval) p;
        if(this.begin==lb.begin&&this.end==lb.end&&
            this.id==lb.id&&this.cid==lb.cid
        )
            return true;
        else
            return false;
    }

}
class IntervalComp implements Comparator<Interval>{

    @Override
    public int compare(Interval s1, Interval s2) {
        if (s1.begin < s2.begin || (s1.begin == s2.begin && s1.end < s2.end )
                || (s1.begin == s2.begin && s1.end == s2.end && s1.id < s2.id)
                || (s1.begin == s2.begin && s1.end == s2.end && s1.id == s2.id && s1.cid < s2.cid))
            return -1;
        return 1;
    }
}

class BInforComp implements Comparator<Binfor>{

    @Override
    public int compare(Binfor b1, Binfor b2) {
        if (b1.rank < b2.rank || (b1.rank == b2.rank && b1.dens < b2.dens)
                || (b1.rank == b2.rank && b1.dens == b2.dens && b1.bid < b2.bid))
            return -1;
        return 1;
    }
}

class strictPairComp implements Comparator<LabelPair>{

    @Override
    public int compare(LabelPair l1, LabelPair l2) {
        if (l1.first < l2.first || (l1.first == l2.first && l1.second < l2.second))
            return -1;
        return 1;
    }
}

class paircomp implements Comparator<LabelPair>{

    @Override
    public int compare(LabelPair l1, LabelPair l2) {
        if (l1.first < l2.first && l1.second < l2.second)
            return -1;
        return 1;
    }
}

class index_comp implements  Comparator<Integer>{
    int[] x;
    index_comp(int[] x){
        this.x=x;
    }

    @Override
    public int compare(Integer a, Integer b) {
        if (x[a]>x[b])
            return -1;
        return 1;
    }
}