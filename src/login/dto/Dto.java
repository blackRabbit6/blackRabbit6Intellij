package login.dto;

public class Dto {
//    Dto =  클라이언트와 서버 간에 데이터를 주고받기 위한 객체
//     로그인 요청 시 사용자가 입력한 ID와 비밀번호를 담은 LoginRequestDTO
//      로그인 성공 시 반환할 사용자 정보를 담은 UserDTO, ManagerDTO 등이 필요
    public static class LoginRequestDTO{
        private String id;
        private String pw;
        public LoginRequestDTO(String id, String pw){
            this.id = id;
            this.pw = pw;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
    public class UserDTO{
        private String id;
        private String name;
        public UserDTO(String id, String name){
            this.id = id;
            this.name = name;
        }

    }
    public class ManagerDTO{
        private String id;
        private String name;
        public ManagerDTO(String id, String name){
            this.id = id;
            this.name = name;
        }
    }
}
