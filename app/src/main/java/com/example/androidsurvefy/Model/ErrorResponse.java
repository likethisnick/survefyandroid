package com.example.androidsurvefy.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorResponse {
    @SerializedName("errors")
    public List<String> errors;
}