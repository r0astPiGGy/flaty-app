package com.rodev.flatyapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.beans.Notify;

public class NotifyViewAdapter extends RecyclerView.Adapter<NotifyViewAdapter.NotifyViewHolder> {

    private final ArrayList<Notify> notifications;

    public NotifyViewAdapter() {
        this(new ArrayList<>());
    }

    public NotifyViewAdapter(ArrayList<Notify> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.notify_card, parent, false);

        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        Notify notify = notifications.get(position);

        holder.setTitle(notify.getTitle());
        holder.setSubject(notify.getContent());
        holder.setDate(notify.getDateMake());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(Collection<Notify> requests) {
        notifications.clear();
        notifications.addAll(requests);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotifyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView subject;
        private final TextView date;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notify_title);
            subject = itemView.findViewById(R.id.notify_subject);
            date = itemView.findViewById(R.id.notify_date);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setSubject(String subject) {
            this.subject.setText(subject);
        }

        public void setDate(Date date) {
            String formattedDate = SimpleDateFormat
                    .getDateInstance(DateFormat.DEFAULT, Locale.forLanguageTag("ru"))
                    .format(date);

            this.date.setText(formattedDate);
        }
    }

}
