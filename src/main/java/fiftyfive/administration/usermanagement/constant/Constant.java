package fiftyfive.administration.usermanagement.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
    public static final String USER_ALREADY_EXISTS = "User already exists with user ID : %s";
    public static final String USER_NOT_EXISTS = "User not exists with user ID : %s";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Value("${grpc.greeting-service}")
    private String address;

    @Value("${grpc.greeting-service.port}")
    private int port;
    private Constant() {

    }
}
