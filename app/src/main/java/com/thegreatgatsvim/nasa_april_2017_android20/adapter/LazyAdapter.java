package com.thegreatgatsvim.nasa_april_2017_android20.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        }else {
            res.recyclable.setText("No Reciclable");
        }

        SharedPreferences mPrefs = context.getSharedPreferences("label", 0);
        String puntos = mPrefs.getString("puntos", "0");
        String latas = mPrefs.getString("latas", "0");
        String botellas = mPrefs.getString("botellas", "0");
        SharedPreferences.Editor mEditor = mPrefs.edit();

        if(rc.getLabel().equals("plastic") ) {
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#9E9E9E"));
            res.recyclable.setTextColor(Color.parseColor("#FFC107"));
            //aumenta en 1 el numero de botellas
            int botellasint = Integer.parseInt(botellas);
            mEditor.putString("botellas", Integer.toString(  botellasint++  ) ).commit();

        }else if(rc.getLabel().equals("can")){
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#9E9E9E"));
            res.recyclable.setTextColor(Color.parseColor("#FFC107"));
            int latasint = Integer.parseInt(latas);
            mEditor.putString("latas", Integer.toString(  latasint++  ) ).commit();
        }else if(rc.getLabel().equals("glass")){
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#9E9E9E"));
            res.recyclable.setTextColor(Color.parseColor("#4CAF50"));
        }else if(rc.getLabel().equals("carton")){
            res.model.setText(rc.getLabel());
            res.model.setTextColor(Color.parseColor("#9E9E9E"));
            res.recyclable.setTextColor(Color.parseColor("#00BCD4"));
        }else{
            res.model.setText(" - - - ");
            res.model.setTextColor(Color.parseColor("#9E9E9E"));
            res.recyclable.setTextColor(Color.parseColor("#00BCD4"));
        }

        res.points.setText(Integer.toString(rc.getScore()) + " PTS");
        //aumenta numero de puntos
        int puntosint = Integer.parseInt(puntos);
        mEditor.putString("puntos", Integer.toString(  puntosint++  ) ).commit();

        return row;
    }
}