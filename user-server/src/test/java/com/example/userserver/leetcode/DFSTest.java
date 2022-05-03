package com.example.userserver.leetcode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/7/20 10:53
 */
public class DFSTest {

    private static boolean flag = false;

    public static void main(String[] args) {
        int n = 3;
        int[][] map = new int[n][n];
        int[][] direction = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        int[][] use = new int[n][n];

//        DFSSearchTarget(map, direction, use, 0,0, n, 0);

        System.out.println(DFSSearchTarget2(direction, use, n, map));
    }

//    public static void main(String[] args) {
//        int n = 3;
//        int[][] map = new int[n][n];
//        int[][] direction = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
//        int[][] use = new int[n][n];
//        System.out.println(BFSSearch(direction, use, n, map));
//    }

    public static void DFSSearchTarget(int[][] map, int[][] direction, int[][] use, int x, int y, int n, int num) {
        System.out.println("x" + x + "y" + y);
        // 找到结果
        if (x == n - 1 && y == n - 1) {
            System.out.println(num);
            flag = true;
            return;
        }

        for (int i = 0; i != direction.length; ++i) {
            int newX = x + direction[i][0];
            int newY = y + direction[i][1];

            if (newX >= 0 && newX < n
                    && newY >= 0 && newY < n
                    && use[newX][newY] == 0 && !flag) {
                use[newX][newY] = 1;
                DFSSearchTarget(map, direction, use, newX, newY, n, num + 1);
                use[newX][newY] = 0;
            }
        }
    }

    public static int DFSSearchTarget2(int[][] direction, int[][] use, int n, int[][] map) {
        Stack<Node> stack = new Stack<>();
        Node node = new Node(0, 0, 0);
        stack.push(node);
        while (!stack.isEmpty()) {
            Node newNode = stack.pop();
            System.out.println("x" + newNode.getX() + "y" + newNode.getY());
            use[newNode.x][newNode.y] = 1;
            for (int i = 0; i < 4; i++) {
                int x = newNode.x + direction[i][0];
                int y = newNode.y + direction[i][1];
                if (x == n - 1 && y == n - 1) {
                    System.out.println("x" + x + "y" + y);
                    return newNode.step + 1;
                }
                if (x >= 0 && y >= 0
                        && x < n && y < n
                        && use[x][y] == 0) {
                    Node next = new Node(x, y, newNode.step + 1);
                    stack.push(next);
                }
            }
        }
        return -1;
    }


    private static int BFSSearch(int[][] direction, int[][] use, int n, int[][] mazeArr) {
        Node node = new Node(0, 0, 0);
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            Node newNode = queue.poll();
            use[newNode.x][newNode.y] = 1;
            for (int i = 0; i < 4; i++) {
                int x = newNode.x + direction[i][0];
                int y = newNode.y + direction[i][1];
                if (x == n - 1 && y == n - 1) {
                    return newNode.step + 1;
                }
                if (x >= 0 && y >= 0
                        && x < n && y < n
                        && use[x][y] == 0) {
                    Node next = new Node(x, y, newNode.step + 1);
                    queue.add(next);
                }
            }
        }
        return -1;
    }

    private static class Node {
        private int x;
        private int y;
        private int step;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public Node(int x, int y, int step) {
            super();
            this.x = x;
            this.y = y;
            this.step = step;
        }
    }

}
