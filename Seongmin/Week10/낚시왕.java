import java.util.*;
import java.io.*;

/*
git commit -m "김성민 / 241010 / [BOJ_낚시왕_17143] / 468ms"
### 풀이 사항
풀이 일자: 2024.10.10
풀이 시간: 2시간 30분
채점 결과: 정답
예상 문제 유형: 구현
시간: 468 ms
메모리: 32248 kb

### 풀이방법
1. 데이터를 입력받는다. (열 상어 정보를 저장하는 리스트 배열에 상어를 저장한다.)
2. 열 개수 만큼 반복한다.
    2-1 해당 열 리스트에서 가장 위에 있는 상어를 제거하고 사이즈만큼 결과에 더한다
    2-2 상어를 이동시킨다.
    2-3 겹치는 상어를 제거한다.
3. 결과를 출력한다.
  
### 느낀점

*/

public class Main {

    static class Shark {
        int r, speed, dir, size;
        int moveTime;

        public Shark(int r, int speed, int dir, int size) {
            this.r = r;
            this.speed = speed;
            this.dir = dir;
            this.size = size;
            moveTime = 0;
        }
    }

    static ArrayList<Shark>[] sharks;
    static int rSize;
    static int cSize;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());
        rSize = Integer.parseInt(st.nextToken());
        cSize = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        sharks = new ArrayList[cSize];
        for (int i = 0; i < cSize; i++) {
            sharks[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            int speed = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            int size = Integer.parseInt(st.nextToken());

            sharks[c].add(new Shark(r, speed, dir, size));
        }
        int result = 0;

        for (int t = 0; t < cSize; t++) {
            //가장 가까운 상어 제거
            int minR = rSize;
            int minIdx = 0;

            for (int i = 0; i < sharks[t].size(); i++) {
                if (minR > sharks[t].get(i).r) {
                    minR = sharks[t].get(i).r;
                    minIdx = i;
                }
            }

            if (minR != rSize) {
                result += sharks[t].remove(minIdx).size;
                //System.out.println(t+"초에 "+ result);
            }
            //상어 이동시키기
            //moveTime이 t와 같은 친구만 이동
//            System.out.println();
            for (int c = 0; c < cSize; c++) {
                for (int r = sharks[c].size()-1; r >= 0; r--) {
                    //방향을 선택
                    if(sharks[c].get(r).moveTime == t){
                        Shark shark = sharks[c].remove(r);
                        shark.moveTime++;
//                        System.out.print(shark.r+":"+c+" / ");
                        int next = move(shark,c);
//                        System.out.println(shark.r+":"+next);
                        sharks[next].add(shark);
                    }
                }
            }
            //겹치는 친구 제거
            for (int c = 0; c < cSize; c++) {
                Shark[] arr = new Shark[rSize];

                for (int i = sharks[c].size()-1; i >= 0; i--) {
                    Shark shark = sharks[c].remove(i);
                    if(arr[shark.r]==null || arr[shark.r].size < shark.size){
                        arr[shark.r] = shark;
                    }
                }
                for (Shark shark : arr) {
                    if(shark != null){
                        sharks[c].add(shark);
                    }
                }
            }
        }


        sb.append(result);

        System.out.print(sb);
    }

    private static int move(Shark shark, int hc) {
        int dir = shark.dir;

        int hr = shark.r;
        int start;
        int size;
        if(dir < 3){
            start = hr;
            size = rSize;
        }else{
            start = hc;
            size = cSize;
        }
        //1상 2하 3우 4
        if(dir%3 == 1) start -= shark.speed;
        else start += shark.speed;

        if(dir < 3) shark.r = getLocation(start,size,shark);
        else hc = getLocation(start,size,shark);

        return hc;
    }

    private static int getLocation(int num, int size, Shark shark) {
        //해당 방향에서 끝까지 가도 끝에 도달하지 못함

        //사이즈가 6 시작이 5 스피드 8 -3
        //-3 1
        while(true){
            if(num>=0 && num<size) return num;
            if(num<0) num *= -1;
            else num = size-1 - (num-size+1);

            shark.dir = switchDir(shark.dir);
        }
    }

    private static int switchDir(int dir) {
        if(dir ==1)return 2;
        else if(dir == 2)return 1;
        else if(dir == 3)return 4;
        else return 3;
    }


}