package com.ynov.dietynov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RVAdapaterMeasurements extends RecyclerView.Adapter<RVAdapaterMeasurements.RVViewHolderMeasurements> {

    private ArrayList<MeasurementData> listMeasurementData;
    private Context mContext;

    public static class RVViewHolderMeasurements extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_date;
        public TextView tv_measurement;
        public LinearLayout parentLayout;

        public RVViewHolderMeasurements(View v) {
            super(v);
            //Ajouter les vues
            tv_date = v.findViewById(R.id.TV_date);
            tv_measurement = v.findViewById(R.id.TV_measurement);

            parentLayout = v.findViewById(R.id.LO_recyclerview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapaterMeasurements(Context _context, ArrayList<MeasurementData> _myDataset) {
        this.mContext = _context;
        listMeasurementData = _myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVAdapaterMeasurements.RVViewHolderMeasurements onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        RVViewHolderMeasurements vh = new RVViewHolderMeasurements(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RVViewHolderMeasurements holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final MeasurementData mData = listMeasurementData.get(position);
        Date date = new Date(mData.getDate()*86400000);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.FRANCE);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        holder.tv_date.setText(formattedDate);
        holder.tv_measurement.setText(Float.toString(mData.getValue()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listMeasurementData.size();
    }
}