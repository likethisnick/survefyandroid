package com.example.androidsurvefy.Network;

import com.example.androidsurvefy.Model.NoteResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NotesApi {
    @GET("Notes")
    Call<NoteResponse> getNotes();
}
