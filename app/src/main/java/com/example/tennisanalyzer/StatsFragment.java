package com.example.tennisanalyzer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;

public class StatsFragment extends Fragment {

    private DigitSpeedView stats_DIGIT_score, stats_DIGIT_swingCount, stats_DIGIT_avrageSwing, stats_DIGIT_maxSwing;
    private int swingMax, swingCount, swingAverage;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_stats, container, false);
        }

        findViews(view);

        return view;
    }

    public void updateStats(int swing) {
        // TODO: 3/4/2020 modify swing to double
        //count
        swingCount++;
        stats_DIGIT_swingCount.updateSpeed(swingCount);
        //average
        swingAverage = (swingAverage + swing) / 2;
        stats_DIGIT_avrageSwing.updateSpeed(swingAverage);
        //max
        if (swingMax < swing) {
            swingMax = swing;
            stats_DIGIT_maxSwing.updateSpeed(swingMax);
        }
    }

    public int getSwingMax() {
        return swingMax;
    }

    public int getSwingCount() {
        return swingCount;
    }

    public int getSwingAverage() {
        return swingAverage;
    }

    private void findViews(View view) {
        stats_DIGIT_score = view.findViewById(R.id.stats_DIGIT_score);
        stats_DIGIT_swingCount = view.findViewById(R.id.stats_DIGIT_swingCount);
        stats_DIGIT_avrageSwing = view.findViewById(R.id.stats_DIGIT_avrageSwing);
        stats_DIGIT_maxSwing = view.findViewById(R.id.stats_DIGIT_maxSwing);
    }
}

