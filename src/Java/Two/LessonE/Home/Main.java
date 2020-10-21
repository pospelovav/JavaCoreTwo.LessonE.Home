package Java.Two.LessonE.Home;

public class Main {
    static final int size  = 10000000;
    static final int h = size / 2;
    static float[] arrayForOneThread = new float[size];
    static float[] arrayForDecomposition = new float[size];
    static long oneThreadRunTime;
    static long manyThreadTimeFinish;


    /**
     *
     * @param arr
     * @param halfSecond
     * Если halfSecond == true, значит имеем дело с правой частью массива и надо считать по-другому
     * Если halfSecond == false, имеем дело либо с целиковым массивом, либо с левой его частью
     */
    public static void arrCalcElements(float[] arr, boolean halfSecond){
        int hDop;
        if (halfSecond){
            hDop = h;
        } else {
            hDop = 0;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + (i + hDop) / 5) * Math.cos(0.2f + (i + hDop) / 5) * Math.cos(0.4f + (i + hDop) / 2));
        }
    }

    public static void arrElementsOne(float[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
    }

    public static void arrOneThread (){
        arrElementsOne(arrayForOneThread);
        long a = System.currentTimeMillis();            // засекаем время начала подсчета элементов
        arrCalcElements(arrayForOneThread, false);
        oneThreadRunTime = System.currentTimeMillis() - a; //вычисляем время подсчета
    }

    public static void arrDecompositionThread () throws InterruptedException {
        arrElementsOne(arrayForDecomposition);

        float[] arrHalfFist = new float[h];
        float[] arrHalfSecond = new float[h];

        long b = System.currentTimeMillis();       //засекаем время декомпозиции массива на два

        System.arraycopy(arrayForDecomposition, 0, arrHalfFist, 0, h);
        System.arraycopy(arrayForDecomposition, h, arrHalfSecond, 0, h);

        Thread t11 = new Thread(new Runnable() {
            @Override
            public void run() {
                arrCalcElements(arrHalfFist,false);    //левая часть массива
            }
        });
        Thread t12 = new Thread(new Runnable() {
            @Override
            public void run() {
                arrCalcElements(arrHalfSecond, true);   //правая часть массива
            }
        });

        t11.start();
        t12.start();
        t11.join();           //ждем когда оба потока закончат
        t12.join();

        System.arraycopy(arrHalfFist, 0, arrayForDecomposition, 0, h);
        System.arraycopy(arrHalfSecond, 0, arrayForDecomposition, h, h);

        manyThreadTimeFinish = System.currentTimeMillis() - b;   //вычисляем время склейки массивов в один после преобразования элементов
    }

    public static void main (String[] args) throws InterruptedException {
        arrOneThread();
        arrDecompositionThread();

        System.out.println("One Thread RunTime = " + oneThreadRunTime);
        System.out.println("Many Thread RunTime = " + manyThreadTimeFinish);

    }
}
