package com.thegreatgatsvim.nasa_april_2017_android20.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.thegreatgatsvim.nasa_april_2017_android20.R;
import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;

import retrofit2.Callback;

public class LazyAdapter extends ArrayAdapter<Recycle>{
    private int layoutId;
    private Context context;
    private List<Recycle> objects;

    public LazyAdapter(Callback<List<Recycle>> callback, int activity_recycle_adapter, List<Recycle> listRecycle) {
        super(null,0);
    }

    public LazyAdapter(Context context, int layoutId,  List<Recycle> objects) {
        super(context, layoutId, objects);

        this.layoutId = layoutId;
        this.context = context;
        this.objects = objects;
    }

    static class ResourceRecycle{
        CircularImageView imageView;
        TextView recyclable;
        TextView model;
        TextView points;
    }

    @Override
    public View getView(int position, View view, ViewGroup group) {
        View row = view;
        ResourceRecycle res = null;

        if (res == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId,group,false);
            res = new ResourceRecycle();

            res.imageView = (CircularImageView) row.findViewById(R.id.imageView);
            res.recyclable = (TextView) row.findViewById(R.id.recycleRecyclable);
            res.model = (TextView) row.findViewById(R.id.recycleModel);
            res.points = (TextView) row.findViewById(R.id.points);

            row.setTag(res);
        }else{
            res = (ResourceRecycle) row.getTag();
        }

        Recycle rc = objects.get(position);

        Picasso.with(context).load(rc.getFeature())
//                .placeholder(R.drawable.default_blur)
//                .error(R.drawable.default_blur)
                .into(res.imageView);

        if (rc.isRecyclable()) {
            res.recyclable.setText("Reciclable");
            res.recyclable.setTextColor(Color.parseColor("#8BC34A"));
        }else {
            res.recyclable.setText("No Reciclable");
            res.recyclable.setTextColor(Color.parseColor("#EF5350"));
        }
        if(rc.getLabel().equals("plastic") || rc.getLabel().equals("can")){
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#FFEE58"));
        }else if(rc.getLabel().equals("glass")){
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#4CAF50"));
        }else{  // carton
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#00BCD4"));
        }
        res.points.setText(Integer.toString(rc.getScore()) + " PTS");

        return row;
    }
}