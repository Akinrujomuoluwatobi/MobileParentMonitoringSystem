package Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.royalteck.progtobi.mpms.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.News;

/**
 * Created by PROG. TOBI on 09-Jun-16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    ArrayList<News> data = new ArrayList<>();

    public NewsAdapter(Context context, ArrayList<News> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    //THIS METHOD REMOVE ITEM FROM THE RECYCLER VIEW IN THE DRAWER LAYOUT.
    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        News current = data.get(position);
        holder.newsheading.setText(current.getHeader());
        holder.newsdate.setText(current.getDate());
        holder.newsdetail.setText(current.getCaption());

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView newsheading, newsdate, newsdetail;
        Button viewnews;
        public MyViewHolder(View itemView) {
            super(itemView);
            newsheading = (TextView) itemView.findViewById(R.id.newsheading);
            newsdate = (TextView) itemView.findViewById(R.id.date);
            newsdetail = (TextView) itemView.findViewById(R.id.news);
            viewnews = (Button) itemView.findViewById(R.id.view_news);
            viewnews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        News clickedDataItem = data.get(pos);
                        final String news = clickedDataItem.getNews();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("");
                        builder.setMessage(news);
                        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                        builder.show();

                    }
                }
            });
            //itemView.setOnClickListener(this);

        }

        //WHEN THE ICON IS CLICKED, IT CALL THE DELETE METHOD THAT REMOVE THE ITEM.
        @Override
        public void onClick(View v) {
            /*context.startActivity(new Intent(context, SubActivity.class));
            if (clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }*/
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
