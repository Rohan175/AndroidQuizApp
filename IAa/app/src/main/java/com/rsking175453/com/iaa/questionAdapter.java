package com.rsking175453.com.iaa;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class questionAdapter extends RecyclerView.Adapter<questionAdapter.SingleItemRowHolder> {

    private ArrayList<qaModel> qaList;
    private Context mContext;
    private ArrayList<qaModel> qaListFiltered;
    private int pi;

    public questionAdapter(Context context, ArrayList<qaModel> qaList) {
        this.qaList = qaList;
        this.mContext = context;
        this.qaListFiltered = qaList;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_question, viewGroup,false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {


        holder.question.setText(qaListFiltered.get(i).getQuestion());
        int count = i+1;
        holder.questionTitle.setText("Question " + count + " :");

        boolean goesDown = (i>pi);
        pi = i;
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.card, "translationY", goesDown==true ? 100 : -100, 0);
        animatorTranslateY.setDuration(500);


        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();

    }

    @Override
    public int getItemCount() {
        return (null != qaListFiltered ? qaListFiltered.size() : 0);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    qaListFiltered = qaList;
                } else {
                    ArrayList<qaModel> filteredList = new ArrayList<>();
                    for (qaModel row : qaList) {


                        if (row.getQuestion().toLowerCase().contains(charString.toLowerCase()) || row.getAnswer().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    qaListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = qaListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                qaListFiltered = (ArrayList<qaModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected CardView card;
        protected TextView question;
        protected TextView questionTitle;

        public SingleItemRowHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.cardQuestion);
            this.question = (TextView) view.findViewById(R.id.questionData);
            this.questionTitle = (TextView) view.findViewById(R.id.questionTitle);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", qaListFiltered);
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putInt("Type",0);

                    FragmentTransaction ft = ((MainActivity)mContext).getFragmentManager().beginTransaction();

                    dialogViewPager newd = dialogViewPager.newInstance();

                    newd.setArguments(bundle);
                    newd.show(ft, "slideshow");

                }
            });


        }

    }
}