package assignment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class balanceKarma {


    public static void main(String args[]){
        double bfs[] = new double[3];

        bfs[0] = ten();
        bfs[1] = fifty();
        bfs[2] = hundred();
        System.out.println(Arrays.toString(bfs));

    }

    public static double ten(){
        double sum = 0;

        for(int k =0; k< 100; k++) {
            int amt = 10;
            boolean done = false;

            Treap<Integer, Integer> tmap = new TreapMap<>();

            Map<Integer, Integer> ints10 = new HashMap<>();
            for (int i = 0; i < amt; i++) {
                do {
                    int rik = (int) (Math.random() * 10000);
                    int riv = (int) (Math.random() * 10000);

                    if (ints10.get(rik) == null) {
                        ints10.put(rik, riv);
                        done = true;
                    }
                } while (!done);
                done = false;
            }

            for (Map.Entry<Integer, Integer> entry : ints10.entrySet())
                tmap.insert(entry.getKey(), entry.getValue());

            sum += tmap.balanceFactor();

            /*if(k==0){
                System.out.println(tmap);
            }*/
        }

        return sum/100.0;
    }

    public static double fifty(){
        double sum = 0;

        for(int k =0; k< 100; k++) {
            int amt = 50;
            boolean done = false;

            Treap<Integer, Integer> tmap = new TreapMap<>();

            Map<Integer, Integer> ints50 = new HashMap<>();
            for (int i = 0; i < amt; i++) {
                do {
                    int rik = (int) (Math.random() * 10000);
                    int riv = (int) (Math.random() * 10000);

                    if (ints50.get(rik) == null) {
                        ints50.put(rik, riv);
                        done = true;
                    }
                } while (!done);
                done = false;
            }

            for (Map.Entry<Integer, Integer> entry : ints50.entrySet())
                tmap.insert(entry.getKey(), entry.getValue());

            sum += tmap.balanceFactor();

            /*if(k==0){
                System.out.println(tmap);
            }*/
        }


        return sum/100.0;
    }

    public static double hundred(){
        double sum = 0;

        for(int k =0; k< 100; k++) {
            int amt = 100;
            boolean done = false;

            Treap<Integer, Integer> tmap = new TreapMap<>();

            Map<Integer, Integer> ints100 = new HashMap<>();
            for (int i = 0; i < amt; i++) {
                do {
                    int rik = (int) (Math.random() * 10000);
                    int riv = (int) (Math.random() * 10000);

                    if (ints100.get(rik) == null) {
                        ints100.put(rik, riv);
                        done = true;
                    }
                } while (!done);
                done = false;
            }

            for (Map.Entry<Integer, Integer> entry : ints100.entrySet())
                tmap.insert(entry.getKey(), entry.getValue());

            sum += tmap.balanceFactor();

            /*if(k==0){
                System.out.println(tmap);
            }*/
        }

        return sum/100.0;
    }

}
