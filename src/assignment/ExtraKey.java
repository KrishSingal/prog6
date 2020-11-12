package assignment;

public class ExtraKey implements Comparable<ExtraKey>{
    public double score;

    public ExtraKey(double s){
        score = s;
    }

    public int compareTo(ExtraKey other){
        if(score > other.score){
            return 1;
        }
        else if(score == other.score){
            return 0;
        }
        else
            return -1;
    }

    public String toString(){
        return "" + score;
    }
}
