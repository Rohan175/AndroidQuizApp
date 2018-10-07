package com.rsking175453.com.iaa;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class dialogViewPager extends DialogFragment {
    private String TAG = dialogViewPager.class.getSimpleName();
    private ArrayList<qaModel> qaData;
    private ArrayList<Integer> result; // 0 for unattempted, 1 for wrong, 2 for correct
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView countText;
    private int selectedPosition = 0;
    private Button next;
    private Button back;

    private  int Type; // Type == 0  Read Mode
                        //Type == 1 MCQ Mode

    static dialogViewPager newInstance() {
        dialogViewPager f = new dialogViewPager();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viewpager, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        countText = (TextView) v.findViewById(R.id.count);
        next = (Button) v.findViewById(R.id.next);
        back = (Button) v.findViewById(R.id.back);


        RelativeLayout l = (RelativeLayout) v.findViewById(R.id.bannerHelp);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://learncodeonline.in/"));
                startActivity(i);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(next.getText().toString()=="Submit"){

                    Intent i = new Intent(getActivity(),resultScreen.class);
                    i.putExtra("result",result);
                    startActivity(i);
                    getActivity().finish();
                }

                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1,true);
            }
        });


        qaData = (ArrayList<qaModel>) getArguments().getSerializable("data");
        selectedPosition = getArguments().getInt("position");
        Type = getArguments().getInt("Type");
        Log.d(TAG, "position: " + selectedPosition);
        Log.d(TAG, "data size: " + qaData.size());

        result = new ArrayList<Integer>(Collections.nCopies(qaData.size(), 0));
        for(int i : result){
            Log.v("result"," " + i);
        }

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        countText.setText((position + 1) + " / " + qaData.size());
        if(position == qaData.size()-1 && Type == 1){
            next.setText("Submit");
        }
        if(position==0){
            back.setVisibility(View.INVISIBLE);
        }else {
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }


    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            Log.v("debug",  "" + position);
            Log.v("debug", qaData.toString());


            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.fullpaged_question, container, false);

            TextView q = (TextView) view.findViewById(R.id.questionDataPage);
            TextView  a = (TextView) view.findViewById(R.id.answerDataPage);

            if(Type == 1) {
                TextView aTitle = (TextView) view.findViewById(R.id.answerDataPage);
                aTitle.setText("Options : ");
                RadioGroup MCQ = (RadioGroup) view.findViewById(R.id.radioGroup);
                view.findViewById(R.id.mcq).setVisibility(View.VISIBLE);

                ArrayList<RadioButton> ra = new ArrayList<RadioButton>();
                ra.add((RadioButton) MCQ.findViewById(R.id.radioButton));
                ra.add((RadioButton) MCQ.findViewById(R.id.radioButton2));
                ra.add((RadioButton) MCQ.findViewById(R.id.radioButton3));
                ra.add((RadioButton) MCQ.findViewById(R.id.radioButton4));


                int index,answer;
                Random n = new Random();

                answer = n.nextInt(4);
                ra.get(answer).setText(qaData.get(position).getAnswer());

                final int AnswerID = ra.get(answer).getId();
                ArrayList<Integer> x = new ArrayList<Integer>();


                for(int i = 0; i < 4; i++){
                    if(i!=answer) {

                        index = n.nextInt(qaData.size());

                        ///To not get repeartted answers

                        if(!x.contains(index)) {
                            x.add(index);
                            ra.get(i).setText(qaData.get(index).getAnswer());
                        }
                    }
                }
                a.setVisibility(View.INVISIBLE);


                MCQ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        if(AnswerID == i){
                            result.add(position,2);
                        }else {
                            result.add(position,1);
                        }

                    }
                });


            }else{
                a.setText(qaData.get(position).getAnswer());
            }

            q.setText(qaData.get(position).getQuestion());

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return qaData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}