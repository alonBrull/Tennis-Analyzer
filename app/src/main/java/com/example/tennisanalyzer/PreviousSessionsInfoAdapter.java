package com.example.tennisanalyzer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PreviousSessionsInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Object> session_info;
    private OnItemClickListenerInfo mItemClickListenerInfo;

    public PreviousSessionsInfoAdapter(Context context, ArrayList<Object> sessionsInfo) {
        this.context = context;
        this.session_info = sessionsInfo;
    }

    public void updateList(ArrayList<Object> sessionsInfo) {
        this.session_info = sessionsInfo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_session_info, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final Object sessionInfo = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            setCardData(holder, session_info);
        }
    }

    private void setCardData(RecyclerView.ViewHolder holder, Object session_info) {
        TextView info = holder.itemView.findViewById(R.id.cardview_TXT_info);
        info.setText("" + session_info);
    }

    @Override
    public int getItemCount() {
        return session_info.size();
    }

    private Object getItem(int position) {
        return session_info.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListenerInfo.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        session_info.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, session_info.size());
    }

    public void SetOnItemClickListenerInfo(final OnItemClickListenerInfo mItemClickListener) {
        this.mItemClickListenerInfo = mItemClickListener;
    }

    public interface OnItemClickListenerInfo {
        void onItemClick(View view, int position, Object sessionInfo);
    }
}