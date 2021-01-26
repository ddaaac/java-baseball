package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class main {
    public static void main(String[] args) throws IOException {
        boolean gameBool = false;
        int selection = -1;

        preMake preCourse = new preMake();
        preCourse.selectNum();

        User user = new User();
        user.getUserNums();
        user.numToArray();

        Game game = new Game(preCourse.array);

        while(true){
            System.out.println(preCourse.array);
            game.compareStrike(user.userArray);
            game.compareBall(user.userArray);
            gameBool = game.gameState();

            if(gameBool){
                selection = user.getUserSelect();
            }

            if(selection < 0){
                user.resetArray();
                user.getUserNums();
                user.numToArray();
                continue;
            }

            if(selection == 0){
                break;
            }

            preCourse = new preMake();
            preCourse.selectNum();

            user = new User();
            user.getUserNums();
            user.numToArray();

            game = game.reset(preCourse.array);
        }
    }
}

class preMake{

    ArrayList<Integer> array = new ArrayList<>();
    Random rd = new Random();

    void selectNum(){
        for(int i = 0; i<3; i++) {
            int temp = rd.nextInt(8)+1;
            array.add(temp);
        }
    }
}

class User{
    int nums;
    ArrayList<Integer> userArray = new ArrayList<>();

    void getUserNums() throws IOException {
        int buffer_num = 0;

        System.out.print("숫자를 입력해주세요 : ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        buffer_num = Integer.parseInt(br.readLine());
        this.nums = buffer_num;
    }

    void numToArray(){
        userArray.add(nums/100);
        userArray.add((nums/10)/10);
        userArray.add(nums%10);
    }

    int getUserSelect() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int selection = Integer.parseInt(br.readLine());

        return selection;
    }

    void resetArray(){
        userArray = new ArrayList<Integer>();
    }
}

class Game{
    int[] arr = new int[2];
    ArrayList<Integer> answer;
    public int[] compareIndex = {1, 2, 0, 2, 0, 1};

    Game(ArrayList<Integer> computer){
        this.answer = computer;
    }

    void compareStrike(ArrayList<Integer> user){
        for(int i = 0; i<3; i++){
            if(answer.get(i) == user.get(i)){
                arr[0] += 1;
            }
        }
    }

    void compareBall(ArrayList<Integer> user){
        for(int i = 0; i<3; i++){
            int index = i*2;
            int userCompareIndexEven = compareIndex[index];
            int userCompareIndexOdd = compareIndex[index+1];

            if(answer.get(i)== user.get(userCompareIndexEven)){
                arr[1]+=1;
            }

            if(answer.get(i) == user.get(userCompareIndexOdd)){
                arr[1]+=1;
            }
        }
    }

    boolean gameState(){
        if(arr[0] == 3){
            System.out.println("3스트라이크");
            System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
            System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
            return true;
        }

        if(arr[0] == 0 && arr[1] == 0){
            System.out.println("낫싱");
            return false;
        }

        if(arr[0] == 0 && arr[1]!=0){
            System.out.println(arr[1]+"볼");
            return false;
        }

        if(arr[0] != 0 && arr[1]==0){
            System.out.println(arr[0]+"스트라이크");
            return false;
        }

        System.out.println(arr[0]+"스트라이크 "+arr[1]+"볼");
        return false;
    }

    Game reset(ArrayList<Integer> newComputerArr){
        Game newGame = new Game(newComputerArr);
        return newGame;
    }
}