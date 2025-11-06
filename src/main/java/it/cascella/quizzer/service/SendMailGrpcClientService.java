package it.cascella.quizzer.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SendMailGrpcClientService {
    private final ManagedChannel channel;
    private Integer emailPort;
    private  String emailDomain;


    public SendMailGrpcClientService(@Value("${microservice.service.email.port}") Integer emailPort,
                                     @Value("${microservice.service.email.domain}") String emailDomain) {
        this.emailPort = emailPort;
        this.emailDomain = emailDomain;
        this.channel = ManagedChannelBuilder
                .forAddress(this.emailDomain, this.emailPort)
                .usePlaintext()
                .build();
    }

    public it.cascella.sendmail.proto.MailSenderServiceGrpc.MailSenderServiceBlockingStub getNewBlockingStub() {
        return it.cascella.sendmail.proto.MailSenderServiceGrpc.newBlockingStub(this.channel);

    }
    public it.cascella.sendmail.proto.MailSenderServiceGrpc.MailSenderServiceFutureStub getNewFutureStub() {
        return it.cascella.sendmail.proto.MailSenderServiceGrpc.newFutureStub(this.channel);

    }
}
