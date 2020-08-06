package com.example.userserver;

public class Test {

    class User {
        private Integer id;
        private String userName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    @org.junit.Test
    public void test() {
        User user = new User();
        user.setId(1);
        user.setUserName("gmy");
        print(user);
    }

    private void print(User user) {
        System.out.println(user.getUserName());
    }

}
