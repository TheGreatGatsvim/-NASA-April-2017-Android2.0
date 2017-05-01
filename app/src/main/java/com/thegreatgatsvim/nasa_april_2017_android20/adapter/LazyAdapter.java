package com.thegreatgatsvim.nasa_april_2017_android20.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.thegreatgatsvim.nasa_april_2017_android20.R;
import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;

public class LazyAdapter extends RecyclerView.Adapter<LazyAdapter.CustomViewHolder>{
    private Context context;
    private List<Recycle> objects;

    public LazyAdapter(Context context, List<Recycle> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycle_adapter, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Recycle rc = objects.get(position);

        Picasso.with(context)
                .load(rc.getFeature())
                .resize(150, 150)
                .centerCrop()
                .into(holder.imageView);

        if (rc.isRecyclable()) {
            holder.recyclable.setText("Recyclable");
        }else {
            holder.recyclable.setText("Non Recycl...");
        }

        SharedPreferences mPrefs = context.getSharedPreferences("label", 0);
        String puntos = mPrefs.getString("puntos", "0");
        String latas = mPrefs.getString("latas", "0");
        String botellas = mPrefs.getString("botellas", "0");
        SharedPreferences.Editor mEditor = mPrefs.edit();

        if(rc.getLabel().equals("plastic") ) {
            holder.model.setText("Plastic");
            holder.model.setTextColor(Color.parseColor("#9E9E9E"));
            holder.recyclable.setTextColor(Color.parseColor("#FFC107"));
            //aumenta en 1 el numero de botellas
            int botellasint = Integer.parseInt(botellas);
            mEditor.putString("botellas", Integer.toString(  botellasint++  ) ).commit();

        }else if(rc.getLabel().equals("can")){
            holder.model.setText("Can");
            holder.model.setTextColor(Color.parseColor("#9E9E9E"));
            holder.recyclable.setTextColor(Color.parseColor("#FFC107"));
            int latasint = Integer.parseInt(latas);
            mEditor.putString("latas", Integer.toString(  latasint++  ) ).commit();
        }else if(rc.getLabel().equals("glass")){
            holder.model.setText("Glass");
            holder.model.setTextColor(Color.parseColor("#9E9E9E"));
            holder.recyclable.setTextColor(Color.parseColor("#4CAF50"));
        }else if(rc.getLabel().equals("carton")){
            holder.model.setText("Cartoon");
            holder.model.setTextColor(Color.parseColor("#9E9E9E"));
            holder.recyclable.setTextColor(Color.parseColor("#00BCD4"));
        }else{
            holder.model.setText(" - - - ");
            holder.model.setTextColor(Color.parseColor("#000000"));
            holder.recyclable.setTextColor(Color.parseColor("#000000"));
        }

        holder.points.setText(Integer.toString(rc.getScore()) + " PTS");
        //aumenta numero de puntos
        int puntosint = Integer.parseInt(puntos);
        mEditor.putString("puntos", Integer.toString(  puntosint++  ) ).commit();
    }

    @Override
    public int getItemCount() {
        return (null != objects ? objects.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        protected CircularImageView imageView;
        protected TextView recyclable;
        protected TextView model;
        protected TextView points;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (CircularImageView) view.findViewById(R.id.imageView);
            this.recyclable = (TextView) view.findViewById(R.id.recycleRecyclable);
            this.model = (TextView) view.findViewById(R.id.recycleModel);
            this.points = (TextView) view.findViewById(R.id.points);
        }
    }
}