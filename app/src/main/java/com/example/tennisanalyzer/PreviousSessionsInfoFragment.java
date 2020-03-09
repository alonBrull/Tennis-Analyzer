package com.example.tennisanalyzer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviousSessionsInfoFragment extends Fragment {

    private Callback_previousSessionInfo callback_previousSessionInfo;
    private View view;
    private RecyclerView sessionsInfoList;
    private ArrayList<Object> infoList = new ArrayList<>();
    private PreviousSessionsInfoAdapter previousSessionsInfoAdapter;

    public void setSessionsInfoList(ArrayList<Object> sessionInfoList){
        this.infoList = sessionInfoList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_previous_sessions_info, container, false);
        }

        sessionsInfoList = view.findViewById(R.id.prev_RCV_sessionInfoList);

        initSessionInfoList();

        return view;
    }

    private void initSessionInfoList() {
        //ArrayList<Object> previousSessions = callback_previousSessionInfo.getSessionInfo(session);
        previousSessionsInfoAdapter = new PreviousSessionsInfoAdapter(getActivity(), infoList);
        sessionsInfoList.setHasFixedSize(true);
        sessionsInfoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        sessionsInfoList.setAdapter(previousSessionsInfoAdapter);
    }
}