//package graphe;
//
//import java.awt.Point;
//import java.io.FileNotFoundException;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import egrep.Radix;
//
//public class Tools {
//    private static DecimalFormat df2 = new DecimalFormat("#.##");
//
//    public static double jaccardDistance(String book1File, String book2File) throws FileNotFoundException {
//
//        Radix r1 = new Radix(book1File);
//        Map<String, ArrayList<Point>> book1 = r1.getIndexTable(book1File);
//
//        Radix r2 = new Radix(book2File);
//        Map<String, ArrayList<Point>> book2 = r2.getIndexTable(book2File);
//        double same = 0.0;
//        double diff = 0.0;
//
//        Iterator itA = book1.entrySet().iterator();
//
//        while (itA.hasNext()) {// 首先拿A中的跟B中的比，A有B无则加入diff中
//            boolean flag = false;
//            Map.Entry eleA = (Map.Entry) itA.next();
//            String word = (String) eleA.getKey();
//            // System.out.println("before comparer diff = "+diff+"; same = "+same);
//            Iterator itB = book2.entrySet().iterator();
//            while (itB.hasNext()) {
//                Map.Entry eleB = (Map.Entry) itB.next();
//                // System.out.println("------>comparer " + eleB.getKey() +" with
//                // "+eleA.getKey());
//                if (eleB.getKey().equals(word)) {// A、B都有，比大小，小的值在same，大的在diff
//                    flag = true;
//                    ArrayList<Point> pB = (ArrayList<Point>) eleB.getValue();
//                    ArrayList<Point> pA = (ArrayList<Point>) eleA.getValue();
//                    // System.out.println("book1 has " + pA.size() +" " +word +" and book2 has
//                    // "+pB.size());
//                    if (pB.size() >= pA.size()) {
//                        same += pA.size();// 1
//                        diff += pB.size();// 2
//                    } else {
//                        same += pB.size();
//                        diff += pA.size();
//                    }
//                }
//                // System.out.println("after comparer >>>>>> diff = "+diff+"; same = "+same);
//            }
//            if (!flag) {
//                // System.out.println("not find "+eleA.getKey() +" in Book2");
//                diff += ((ArrayList<Point>) eleA.getValue()).size();
//            }
//        }
//
//        Iterator itB2 = book2.entrySet().iterator();
//
//        while (itB2.hasNext()) {
//            Map.Entry eleB2 = (Map.Entry) itB2.next();
//            String word = (String) eleB2.getKey();
//            boolean f = false;
//            Iterator itA2 = book1.entrySet().iterator();
//            while (itA2.hasNext()) {
//                Map.Entry eleA2 = (Map.Entry) itA2.next();
//                if (eleA2.getKey().equals(word)) {
//                    f = true;
//                    break;
//                }
//            }
//            if (!f) {
//                // System.out.println("book1 has not "+eleB2.getKey() +" book2 has " +
//                // ((ArrayList<Point>)eleB2.getValue()).size());
//                diff += ((ArrayList<Point>) eleB2.getValue()).size();
//            }
//        }
//        // System.out.println("diff = "+diff+"; same = "+same);
//        double d = (diff - same) / diff;
//        return d;
//    }
//
//
//    public static double jaccardDistance2(String book1File, String book2File) {
//        try {
//            double dis = 0;
//            Radix r1 = new Radix(book1File);
//            Map<String, Integer> book1 = r1.getIndexTable2(book1File);
//
//            Radix r2 = new Radix(book2File);
//            Map<String, Integer> book2 = r2.getIndexTable2(book2File);
//            double d1 = 0.0;
//            double d2 = 0.0;
//
//            for (Entry<String, Integer> e : book1.entrySet()) {
//                String word = e.getKey();
//                d1 += Math.abs((e.getValue() - (book2.containsKey(word) ? book2.get(word) : 0)));
//                d2 += Math.max(e.getValue(), (book2.containsKey(word) ? book2.get(word) : 0));
//                book2.remove(word);
//            }
//
//            for (Entry<String, Integer> e : book2.entrySet()) {
//                String word = e.getKey();
//                d1 += Math.abs((e.getValue() - (book1.containsKey(word) ? book1.get(word) : 0)));
//                d2 += Math.max(e.getValue(), (book1.containsKey(word) ? book1.get(word) : 0));
//            }
//            //System.out.println("d1 = "+d1+"  d2="+d2);
//            return Math.round((d2 != 0 ? (d1 / d2) : 0) * 100.0) / 100.0;
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            return 0;
//        }
//    }
//
//    public static double[][] grapheJaccard(ArrayList<String> files) {
//        double[][] graphe = new double[files.size()][files.size()];
//        for (int i = 0; i < files.size(); i++) {
//            for (int j = i; j < files.size(); j++) {
//                double dis = jaccardDistance2(files.get(i), files.get(j));
//                graphe[i][j] = dis;
//                graphe[j][i] = dis;
//            }
//        }
//        return graphe;
//    }
//
//    public static double[][] floydWarshall1(double[][] grapheJaccard) {
//        double[][] tab = grapheJaccard;
//        for (int k = 0; k < tab.length; k++) {
//            for (int i = 0; i < tab.length; i++) {
//                for (int j = 0; j < tab.length; j++) {
//                    if (tab[i][j] > tab[i][k] + tab[k][j]) {
//                        tab[i][j] = tab[i][k] + tab[k][j];
//                    }
//                }
//            }
//        }
//        return tab;
//    }
//
//    public static double[] closeness(double[][] paths) {
//        double[] closenessTab = new double[paths.length];
//        for (int i = 0; i < closenessTab.length; i++) {
//            closenessTab[i] = 0;
//            for (int j = 0; j < paths.length; j++) {
//                closenessTab[i] += paths[i][j];
//            }
//            closenessTab[i] = (double) paths.length / closenessTab[i];
//        }
//        return closenessTab;
//    }
//
//    public static double closseness2(String fileName, ArrayList<String> Library) throws FileNotFoundException {
//        int Book_NUMBER = Library.size();
//        double distance = 0;
//        for (int i = 0; i < Book_NUMBER; i++) {
//            distance += jaccardDistance2(fileName, Library.get(i));
//        }
//        return (Book_NUMBER) / distance;
//    }
//
//    public static double[] clossnessGraph(ArrayList<String> Library) throws FileNotFoundException {
//        double[] graphe = new double[Library.size()];
//        for (int i = 0; i < Library.size(); i++) {
//            graphe[i] = closseness2(Library.get(i), Library);
//        }
//        return graphe;
//    }
//
//    public static int[][] calculShortestPaths(double[][] grapheJaccard) {
//        int[][] paths = new int[grapheJaccard.length][grapheJaccard.length];//path用来记录经过谁，dist用来记录距离
//        for (int i = 0; i < paths.length; i++) for (int j = 0; j < paths.length; j++) paths[i][j] = i;
//
//        double[][] dist = new double[grapheJaccard.length][grapheJaccard.length];
//
//        for (int i = 0; i < paths.length; i++) {
//            for (int j = 0; j < paths.length; j++) {
//                if (i == j) {
//                    dist[i][i] = 0;
//                    continue;
//                }
//                if (grapheJaccard[i][j] <= 0.75)
//                    dist[i][j] = grapheJaccard[i][j];
//                else dist[i][j] = Double.POSITIVE_INFINITY;
//                paths[i][j] = j;
//            }
//        }
//        for (int k = 0; k < paths.length; k++) {
//            for (int i = 0; i < paths.length; i++) {
//                for (int j = 0; j < paths.length; j++) {
//                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
//                        dist[i][j] = dist[i][k] + dist[k][j];
//                        paths[i][j] = paths[i][k];//即paths[i][j] = k;
//                    }
//                }
//            }
//        }
//        return paths;
//    }
//
//    //这种方法还是有缺陷，例如:a->h 可能有以下两种： a->b->c->e->f->h 或者 a->b->d->g->f->h 以下方法只记录了一种
////    public static int[][] floydWarshall2(double[][] grapheJaccard) {
////        int[][] Path = new int[grapheJaccard.length][grapheJaccard.length];
////        double[][] dist = new double[grapheJaccard.length][grapheJaccard.length];
////
////        for (int i = 0; i < Path.length; i++) {
////            for (int j = 0; j < Path.length; j++) {
////                if (i == j) {
////                    dist[i][i] = 0;
////                    continue;
////                }
////                if(grapheJaccard[i][j]< 0.75) dist[i][j] = grapheJaccard[i][j];
////                else dist[i][j] = Double.POSITIVE_INFINITY;
////                Path[i][j] = j;
////            }
////        }
////        for (int k = 0; k < grapheJaccard.length; k++) {
////            for (int i = 0; i < grapheJaccard.length; i++) {
////                for (int j = 0; j < grapheJaccard.length; j++) {
////                    if (grapheJaccard[i][j] > grapheJaccard[i][k] + grapheJaccard[k][j]) {
////                        grapheJaccard[i][j] = grapheJaccard[i][k] + grapheJaccard[k][j];
////                        Path[i][j] = Path[i][k];
////                    } else if (grapheJaccard[i][j] == grapheJaccard[i][k] + grapheJaccard[k][j]) {
////                        Path[i][j] = Path[i][k];
////                        //to complete
////                    }
////                }
////            }
////        }
////        return Path;
////    }
//
//    public static boolean passerK(int[][] path, int k,int i,int j){//tester le path est passer par K
//        if(path[i][j] == j) return false;
//        else{
//            int p = path[i][j];
//            while(p!=j && p!=k){
//                p = path[p][j];
//            }
//            if(p == k) return true;
//        }
//        return false;
//    }
//
//    public static int numberPasserK(int[][] path,double[][] grapheJaccard, int k,int j){
//        int Sum = 1;
//        if(path[k][j] == j) return Sum;
//        else{
//            int p = path[k][j];
//            if(grapheJaccard[k][j]>grapheJaccard[k][p]+grapheJaccard[p][j]){
//                return Sum;
//            }else if(grapheJaccard[k][j]==grapheJaccard[k][p]+grapheJaccard[p][j]){
//                Sum = 2;
//                return Sum;
//            }
//            while(p!=j){
//                Sum *= calculNumberOfPath(path,grapheJaccard,p,j);
//                p = path[p][j];
//            }
//        }
//        return Sum;
//    }
//
//    public static int calculNumberOfPath(int[][] path, double[][] grapheJaccard,int i,int j){//从k开始到j有多少条路
//        int Sum = 1;
//        if(path[i][j] == j) return Sum;
//        else{
//            int p = path[i][j];
//            if(grapheJaccard[i][j]>grapheJaccard[i][p]+grapheJaccard[p][j]){
//                return Sum;
//            }else if(grapheJaccard[i][j]==grapheJaccard[i][p]+grapheJaccard[p][j]){
//                Sum = 2;
//                return Sum;
//            }
//            while(p!=j){
//                Sum *= calculNumberOfPath(path,grapheJaccard,p,j);
//                p = path[p][j];
//            }
//        }
//        return Sum;
//    }
//
//    public static double cal(int[][] path, double[][] grapheJaccard, int k) {
//        double bet =0;
//        for (int i = 0; i < path.length; i++) {//计算从i出发的每条路有几条经过k
//            if(i == k) break;
//            int SumK = 0,count = 0;
//            for (int j = i + 1; j < path.length; j++) {
//                if(j == k) break;
//                count = calculNumberOfPath(path,grapheJaccard,i,j);
//                if(passerK(path,k,i,j)){
//                    SumK = numberPasserK(path,grapheJaccard,k,j);
//                }
//            }
//            if(count != 0) {
//                bet += SumK / count;
//            }
//        }
//        return bet;
//    }
//
//    public static double[] betwennesse(double[][] grapheJaccard) {
//        double betwennesse[] = new double[grapheJaccard.length];
//        int [][] path = calculShortestPaths(grapheJaccard);
//        for(int i=0;i<grapheJaccard.length;i++){
//            betwennesse[i] = cal(path,grapheJaccard,i);
//        }
//        return betwennesse;
//    }
//
//    public static double[] betwennesseNormaliser(double[] betwennesse){
//        double betwennesseN[] = new double[betwennesse.length];
//        double Max = 0,Min = Double.MAX_VALUE;
//        for(double d:betwennesse){
//            if (d<Min) Min = d;
//            if(d>Max) Max = d;
//        }
//        for(int i=0;i<betwennesse.length;i++){
//            betwennesseN[i] = (betwennesse[i]-Min)/(Max-Min);
//        }
//        return betwennesseN;
//    }
//
//}
