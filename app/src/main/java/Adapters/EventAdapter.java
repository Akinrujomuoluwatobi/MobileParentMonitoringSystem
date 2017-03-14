package Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.royalteck.progtobi.mpms.R;

import java.util.ArrayList;

import Model.Event;
import Model.News;

/**
 * Created by PROG. TOBI on 09-Jun-16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ClickListener clickListener;

    ArrayList<Event> data = new ArrayList<>();

    public EventAdapter(Context context, ArrayList<Event> data) {
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
        View view = inflater.inflate(R.layout.events, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event current = data.get(position);
        holder.eventtitle.setText(current.getHeader());
        holder.eventvenue.setText(current.getVenue());
        holder.eventdate.setText(current.getEventdate());
        holder.eventtime.setText(current.getEventtime());
        holder.eventcaption.setText(current.getCaption());

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView eventtitle, eventvenue, eventdate, eventtime, eventcaption;
        public MyViewHolder(View itemView) {
            super(itemView);
            eventtitle = (TextView) itemView.findViewById(R.id.eventtitle);
            eventvenue = (TextView) itemView.findViewById(R.id.eventvenueview);
            eventdate = (TextView) itemView.findViewById(R.id.eventdate);
            eventtime = (TextView) itemView.findViewById(R.id.eventtime);
            eventcaption = (TextView) itemView.findViewById(R.id.eventcaptionview);

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
