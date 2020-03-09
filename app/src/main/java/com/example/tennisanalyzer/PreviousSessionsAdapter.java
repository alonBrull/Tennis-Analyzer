package com.example.tennisanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviousSessionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Session> sessions;
    private OnItemClickListener mItemClickListener;
    private boolean isInfo;

    public PreviousSessionsAdapter(Context context, ArrayList<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    public void updateList(ArrayList<Session> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_session, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final Session session = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            setCardData(holder, session);
        }
    }

    private void setCardData(RecyclerView.ViewHolder holder, Session session) {
        TextView date = holder.itemView.findViewById(R.id.cardview_TXT_date);
        date.setText(session.getDate());

        TextView max = holder.itemView.findViewById(R.id.cardview_TXT_swingMax);
        max.setText("max power : " + session.getSwingMax()+ "|");

        TextView average = holder.itemView.findViewById(R.id.cardview_TXT_swingAverage);
        average.setText("average : " + session.getSwingAverage()+ "|");

        TextView count = holder.itemView.findViewById(R.id.cardview_TXT_swingCount);
        count.setText("swings : " + session.getSwingCount()+ "|");

        TextView length = holder.itemView.findViewById(R.id.cardview_TXT_length);
        length.setText("length : " + session.getLength());

        RelativeLayout card = holder.itemView.findViewById(R.id.cardview_RLY_sessionData);

        final LinearLayout dateLayout = holder.itemView.findViewById(R.id.cardview_LY_date);
        final LinearLayout infoLayout = holder.itemView.findViewById(R.id.cardview_LY_info);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInfo) {
                    dateLayout.setVisibility(View.VISIBLE);
                    infoLayout.setVisibility(View.INVISIBLE);
                    isInfo = false;
                }

                else {
                    dateLayout.setVisibility(View.INVISIBLE);
                    infoLayout.setVisibility(View.VISIBLE);
                    isInfo = true;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private Session getItem(int position) {
        return sessions.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        sessions.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sessions.size());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Session session);
    }

}
