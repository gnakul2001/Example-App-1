package com.nakultelestock.exampleapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendAndRequestResponse();
    }

    private void sendAndRequestResponse() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://gh-trending-api.herokuapp.com/repositories";
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                ArrayList<MyListData> arrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    arrayList.add(new MyListData( jsonObject.getString("username") + "/" + jsonObject.getString("repositoryName") ,jsonObject.getString("description") , jsonObject.getString("language"), jsonObject.getString("totalStars"), jsonObject.getString("forks")));
                }
                MyListAdapter adapter = new MyListAdapter(arrayList);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Error :" + error.toString(), Toast.LENGTH_SHORT).show());
        mRequestQueue.add(mStringRequest);
    }

}

class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private final ArrayList<MyListData> listdata;
    int selected_position = -1;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recyclerview_list, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MyListData myListData = listdata.get(position);
        holder.repoTitle.setText(myListData.getRepoTitle());
        holder.repoDesc.setText(myListData.getRepoDesc());
        holder.repoLanguage.setText(myListData.getRepoLanguage());
        holder.totalStars.setText(myListData.getTotalStars());
        holder.repoForks.setText(myListData.getRepoForks());
        if (position == selected_position) {
            holder.card_view.setStrokeColor(Color.parseColor("#28214d"));
            holder.card_view.setStrokeWidth(3);
        } else {
            holder.card_view.setStrokeColor(Color.parseColor("#ffffff"));
            holder.card_view.setStrokeWidth(0);
        }
        holder.card_view.setOnClickListener(view -> {
            selected_position = position;
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView repoTitle;
        public TextView repoDesc;
        public TextView repoLanguage;
        public TextView totalStars;
        public TextView repoForks;
        public MaterialCardView card_view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.repoTitle = itemView.findViewById(R.id.repoTitle);
            this.repoDesc = itemView.findViewById(R.id.repoDesc);
            this.repoLanguage = itemView.findViewById(R.id.repoLanguage);
            this.totalStars = itemView.findViewById(R.id.totalStars);
            this.repoForks = itemView.findViewById(R.id.repoForks);
            this.card_view = itemView.findViewById(R.id.card_view);
        }
    }
}

class MyListData {
    String repoTitle;
    String repoDesc;
    String repoLanguage;
    String totalStars;
    String repoForks;

    public MyListData(String repoTitle, String repoDesc, String repoLanguage, String totalStars, String repoForks) {
        this.repoTitle = repoTitle;
        this.repoDesc = repoDesc;
        this.repoLanguage = repoLanguage;
        this.totalStars = totalStars;
        this.repoForks = repoForks;
    }

    public String getRepoTitle() {
        return repoTitle;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public String getRepoLanguage() {
        return repoLanguage;
    }

    public String getTotalStars() {
        return totalStars;
    }

    public String getRepoForks() {
        return repoForks;
    }
}