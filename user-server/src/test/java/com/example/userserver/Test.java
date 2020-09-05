package com.example.userserver;

public class Test {

    private static final String ZERO16 = "0000000000000000";

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
//        User user = new User();
//        user.setId(1);
//        user.setUserName("gmy");
//        print(user);

        Boolean exist = true;
        if(exist == null || !exist) {
            System.out.println("111111111111111");
        }

        String random = this.getFormatNumber(4, 1);

    }

    private String getFormatNumber(int length, int num) {
        String strNum = String.valueOf(num);
        String prefix = ZERO16.substring(0, length - strNum.length());
        return prefix + strNum;
    }

    private void print(User user) {
        System.out.println(user.getUserName());
    }

}
