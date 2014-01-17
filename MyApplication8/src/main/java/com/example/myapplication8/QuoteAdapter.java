package com.example.myapplication8;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SBaar on 12/30/13.
 */
public class QuoteAdapter extends ArrayAdapter<QuoteModel>  {

    private final Context context;
    private final List<QuoteModel> values;
    private final Markov m;
    public QuoteAdapter(Context context, List<QuoteModel> values, Markov markov) {
        super(context, R.layout.genline, values);
        this.context = context;
        this.values = values;

        this.m = markov;
    }
    static class QuoteHolder{
        protected TextView textView;
        protected ImageButton imageButton;
        protected View toolbar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.genline, parent, false);
            final QuoteHolder quoteHolder = new QuoteHolder();
            quoteHolder.textView = (TextView) view.findViewById(R.id.textView);
           // quoteHolder.toolbar = view.findViewById(R.id.toolbar);
            quoteHolder.imageButton = (ImageButton) view.findViewById(R.id.starButton);
            quoteHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setSelected(!view.isSelected());
                    QuoteModel element = (QuoteModel) quoteHolder.imageButton.getTag();
                    element.setSelected(view.isSelected());
                }
            });
            view.setTag(quoteHolder);
            quoteHolder.imageButton.setTag(values.get(position));

        }
        else
        {
            view = convertView;
            ((QuoteHolder) view.getTag()).imageButton.setTag(values.get(position));
        }
        QuoteHolder quoteHolder = (QuoteHolder) view.getTag();
        quoteHolder.textView.setText(values.get(position).getText());
        quoteHolder.imageButton.setSelected(values.get(position).isSelected());

         if (position % 2 == 1)
            view.setBackgroundColor(Color.rgb(225,225,225));
        else
            view.setBackgroundColor(Color.rgb(255,255,255));
            return view;
    }


}