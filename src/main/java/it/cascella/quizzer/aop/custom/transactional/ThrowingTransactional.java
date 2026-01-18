package it.cascella.quizzer.aop.custom.transactional;


import jakarta.transaction.Transactional;

@Transactional

public @interface ThrowingTransactional {
}
