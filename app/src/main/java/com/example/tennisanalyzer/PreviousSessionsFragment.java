package com.example.tennisanalyzer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviousSessionsFragment extends Fragment {

    private Callback_previousSessions callback_previousSessions;
    private View view;
    private RecyclerView sessionsList;
    private PreviousSessionsAdapter previousSessionsAdapter;

    public void setCallback(Callback_previousSessions callback) {
        callback_previousSessions = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_previous_sessions, container, false);
        }

        sessionsList = view.findViewById(R.id.prev_RCV_sessionList);

        initSessionList();
        
        return view;
    }

    private void initSessionList() {
        ArrayList<Session> previousSessions = callback_previousSessions.getPreviousSessions();
        previousSessionsAdapter = new PreviousSessionsAdapter(getActivity(), previousSessions);
        sessionsList.setHasFixedSize(true);
        sessionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        sessionsList.setAdapter(previousSessionsAdapter);
        previousSessionsAdapter.SetOnItemClickListener(new PreviousSessionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Session session) {
                // no real need for this
            }
        });
    }
}