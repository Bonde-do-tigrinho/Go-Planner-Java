package com.go.go_planner.application.port.in;

public interface ConfirmAccountUseCase {
    void confirmAccount(ConfirmAccountCommand command);
    record ConfirmAccountCommand(String email, String codigo) {}
}
