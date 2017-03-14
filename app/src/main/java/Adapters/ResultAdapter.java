package Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Model.Results;
import com.royalteck.progtobi.mpms.R;

import java.util.ArrayList;

/**
 * Created by PROG. TOBI on 07-Jul-16.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.RecyclerViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;

    ArrayList<Results> arrayList = new ArrayList<>();

    public ResultAdapter(ArrayList<Results> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_header_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, viewType);
            return recyclerViewHolder;
        } else if (viewType == TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, viewType);

            return recyclerViewHolder;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.viewType == TYPE_LIST) {
            Results results = arrayList.get(position - 1);
            holder.subject.setText(results.getSubject());
            holder.ca1.setText(Integer.toString(results.getCa1()));
            holder.ca2.setText(Integer.toString(results.getCa2()));
            holder.exam.setText(Integer.toString(results.getExam()));
            holder.grade.setText(results.getGrade());
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1 ;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView subject, ca1, ca2, exam, grade;
        int viewType;

        public RecyclerViewHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_LIST) {
                subject = (TextView) view.findViewById(R.id.subjecttxtview);
                ca1 = (TextView) view.findViewById(R.id.ca1txtview);
                ca2 = (TextView) view.findViewById(R.id.ca2txtview);
                exam = (TextView) view.findViewById(R.id.examtxtview);
                grade = (TextView) view.findViewById(R.id.gradetxtview);
                this.viewType = TYPE_LIST;
            } else if (viewType == TYPE_HEAD) {
                this.viewType = TYPE_HEAD;
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
        return TYPE_LIST;
    }
}
