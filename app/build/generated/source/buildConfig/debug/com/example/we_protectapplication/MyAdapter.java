package com.example.we_protectapplication;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//this is the custom adapter for recycler view to show rows in news Screen
class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    private ArrayList<NewsModel> infoList = new ArrayList<>();
    private LayoutInflater mInflater;
    Context context;
    NewsModel currentItem;
    NewsModel getCurrentItem;


    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }


//this method set new data to list
    public void setData(ArrayList<NewsModel> list) {
        this.infoList = list;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override//this method give view to the ist
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rowdata, parent, false);
        MyviewHolder viewHolder = new MyviewHolder(view);
        return viewHolder;
    }



    @Override//this method binds the data to list or fills the data to the list
    public void onBindViewHolder(MyviewHolder holder, final int position) {
        currentItem = infoList.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDescription());
      //  Picasso.get()
            //    .load(currentItem.getImage())
            //    .resize(50, 50)
             //   .centerCrop()
             //   .into(holder.imageView);



    }


    @Override
    public int getItemCount() {
        return infoList.size();
    }
    public String getCount() {
        String count= Integer.toString(infoList.size());
        return count;
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;




        public MyviewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);



        }
    }

}
