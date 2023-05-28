package com.example.cloud.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonAutoDetect
@AllArgsConstructor
public class EditFileNameRequest {

    private String filename;
}
