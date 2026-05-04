package com.splithome.backend.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(int status,
                               String message,
                               LocalDateTime timeStamp) {
}
