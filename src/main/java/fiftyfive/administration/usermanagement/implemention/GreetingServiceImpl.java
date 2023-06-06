package fiftyfive.administration.usermanagement.implemention;

import com.techprimers.grpc.GreetingRequest;
import com.techprimers.grpc.GreetingResponse;
import com.techprimers.grpc.GreetingServiceGrpc;
import fiftyfive.administration.usermanagement.constant.Constant;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GreetingServiceImpl {


    private final ManagedChannel channel;
    private final GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub;

    public GreetingServiceImpl(final Constant constant) {
        // Create a channel to connect to the gRPC server
        channel = ManagedChannelBuilder.forAddress(constant.getAddress(), constant.getPort())
                .usePlaintext()
                .build();

        // Create a blocking stub for making synchronous gRPC calls
        blockingStub = GreetingServiceGrpc.newBlockingStub(channel);
    }


    // ... other methods in your service ...

    public  Map<String,String> callGRPCApi(String message) {
        GreetingRequest request = GreetingRequest.newBuilder()
                .setMessage(message)
                .build();

        GreetingResponse response;
        try {
            response = blockingStub.greeting(request);
        } catch (RuntimeException exception) {
            System.out.printf(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

        Map<String,String> resp= new HashMap<>();
        resp.put("message",response.getMessage());
        return resp;
    }

    public void shutdown() {
        // Shutdown the gRPC channel when the service is shutting down
        channel.shutdownNow();
    }
}
