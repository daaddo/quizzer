package it.cascella.quizzer.dtos;

import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Base64;

@Slf4j
@Getter
@Setter
@NoArgsConstructor

public class IssuedQuizInfosDto {

    private byte[] tokenId; // campo mappato dal DB
    private Integer issuerId;
    private Integer quizId;
    private Integer numberOfQuestions;
    private Timestamp issuedAt;
    private Timestamp expiresAt;
    private Time duration;


    public IssuedQuizInfosDto(byte[] tokenId, Integer issuerId, Integer quizId, Integer numberOfQuestions, Timestamp issuedAt, Timestamp expiresAt, Time duration) {
        this.tokenId = tokenId;
        this.issuerId = issuerId;
        this.quizId = quizId;
        this.numberOfQuestions = numberOfQuestions;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.duration = duration;
    }

    @JsonProperty("tokenId")
    public String getTokenIdAsBase64() {
        String token = new String(tokenId, StandardCharsets.UTF_8);

        log.info("getTokenIdAsBase64 token: {}, decoded:{}",tokenId,  token);
        return tokenId != null ? token : null;
    }



}