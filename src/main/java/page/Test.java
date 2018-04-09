package page;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author sunzequn
 * @create 2018-04-08 下午12:25
 **/
public class Test {

    private void t(int n){
        List<String> l = new ArrayList<>();
        for (int i = n; i < n+10; i++) {
            l.add(i + "");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(l);

    }

    public static void main(String[] args) {
        Test t = new Test();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                t.t(0);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                t.t(10);
            }
        });


        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                t.t(20);
            }
        });


        t1.start();
        t2.start();
        t3.start();
    };

}
